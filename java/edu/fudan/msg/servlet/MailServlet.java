package edu.fudan.msg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.fudan.msg.constant.JSONResult;
import edu.fudan.msg.constant.RequestParameter;
import edu.fudan.msg.pojo.Mail;
import edu.fudan.msg.pojo.Student;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.service.MailService;
import edu.fudan.msg.service.StudentService;
import edu.fudan.msg.util.EmailUtil;
import edu.fudan.msg.util.TimeStampUtil;
import edu.fudan.msg.util.bean.Email;

public class MailServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager.getLogger(MailServlet.class);

	private MailService mailService;
	private StudentService studentService;

	private static final int SUM = 50;

	public MailServlet() {
		mailService = new MailService();
		studentService = new StudentService();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String action = request.getParameter("action");
		JSONObject json = null;

		if (null == action || 1 != action.length()) {
			json = JSONResult.INVALID_PARAMETER;
		} else {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			char key = action.charAt(0);
			switch (key) {
			case RequestParameter.CREATE:
				json = send(request, user);
				break;
			case RequestParameter.RETRIEVE:
				json = getHistoryEmail(request, user);
				break;
			// case RequestParameter.RETRIEVE:
			// json = getHistoryEmail(request, user);
			// break;
			default:
				json = JSONResult.INVALID_PARAMETER;
			}
		}

		LOG.info(json);
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.flush();
		out.close();

	}

	private JSONObject getHistoryEmail(HttpServletRequest request, User user) {
		int uid = user.getId();
		String sender = user.getName();
		List<Mail> mails = mailService.getMailsBySender(uid);
		if (mails == null) {
			return JSONResult.NO_DATA;
		}

		JSONArray array = objectToJson(mails, sender);
		JSONObject json = JSONResult.success();
		json.accumulate(JSONResult.DATA, array);
		return json;
	}

	private JSONObject send(HttpServletRequest request, User user) {
		String ids[] = request.getParameterValues("ids[]");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String sender_email = user.getEmail();

		JSONObject json = new JSONObject();
		if (ids == null || title == null || content == null) {
			return JSONResult.INVALID_PARAMETER;
		}

		Email email = new Email();
		email.setContent(content);
		email.setTitle(title);

		email.setCopyTo(sender_email);

		StringBuffer buffer = new StringBuffer();
		Student student = null;
		JSONArray array = new JSONArray();
		ArrayList<Integer> receivers = new ArrayList<Integer>();
		int count = 0;
		for (int i = 0; i < ids.length; i++) {
			int id = Integer.parseInt(ids[i]);
			student = studentService.findStu(id);
			count++;
			if (student == null) {
				array.add(id);
			} else {
				String receiver = student.getEmail();
				buffer.append(receiver + ",");
				receivers.add(id);
			}

			if (count % SUM == 0) {
				email.setTo(buffer.toString());
				buffer.setLength(0);
				try {
					EmailUtil.send(email);
					json = JSONResult.success();
				} catch (Exception e) {
					json.accumulate("Mail", "Mail send error.");
				}
			}

		}
		email.setTo(buffer.toString());
		try {
			EmailUtil.send(email);
			json = JSONResult.success();
		} catch (Exception e) {
			json.accumulate("Mail", "Mail send error.");
		}

		if (array != null) {
			json.accumulate("FAIL_INVAILD_ID", JSONArray.fromObject(array));
		}

		/**
		 * save mail to database
		 */
		Mail mail = new Mail();
		mail.setContent(content);
		mail.setSender(user.getId());
		mail.setTopic(title);
		mail.setReceivers(receivers);
		String createTime = TimeStampUtil.TimestampToString();
		mail.setSendTime(createTime);
		int result = mailService.addMail(mail);
		if (result <= 0) {
			json.accumulate("ERROR", "Update database error.");
		}
		return json;
	}

	private JSONArray objectToJson(List<Mail> mails, String sender) {
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		ArrayList<Integer> receivers = null;
		Student student = null;
		ArrayList<String> receivers_str = new ArrayList<String>();
		for (Mail mail : mails) {
			receivers = mail.getReceivers();
			for (int i = 0; i < receivers.size(); i++) {
				student = studentService.findStu(receivers.get(i));
				String receiver = student.getName();
				receivers_str.add(receiver);
			}

			json.accumulate("topic", mail.getTopic());
			json.accumulate("sender", sender);
			json.accumulate("id", mail.getId());
			json.accumulate("content", mail.getContent());
			json.accumulate("sendTime", mail.getSendTime());
			json.accumulate("receivers", JSONArray.fromObject(receivers_str));
			receivers_str.clear();
			array.add(json);
			json.clear();

		}
		return array;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}