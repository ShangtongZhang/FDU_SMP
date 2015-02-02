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
import edu.fudan.msg.pojo.Message;
import edu.fudan.msg.pojo.Student;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.service.MessageService;
import edu.fudan.msg.service.StudentService;
import edu.fudan.msg.util.TimeStampUtil;

public class MessageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager
			.getLogger(MessageServlet.class);

	private MessageService messageService;
	private StudentService studentService;

	public MessageServlet() {
		messageService = new MessageService();
		studentService = new StudentService();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json;charset-utf-8");
		JSONObject json = null;
		String action = request.getParameter("action");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (null == action || 1 != action.length()) {
			json = JSONResult.INVALID_PARAMETER;
		} else {
			char key = action.charAt(0);
			switch (key) {
			case RequestParameter.CREATE:
				json = sendMessage(request, user);
				break;
			case RequestParameter.RETRIEVE:
				json = getHistoryMessage(request, user);
				break;
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

	private JSONObject getHistoryMessage(HttpServletRequest request, User user) {
		int uid = user.getId();
		List<Message> messages = messageService.getMsgsBySender(uid);
		if (messages == null) {
			return JSONResult.NO_DATA;
		}
		JSONArray array = objectToJson(messages, user.getName());
		JSONObject json = JSONResult.success();
		json.accumulate(JSONResult.DATA, array);
		return json;
	}

	private JSONObject sendMessage(HttpServletRequest request, User user) {
		String ids[] = request.getParameterValues("ids[]");
		String content = request.getParameter("content");

		if (ids == null || content == null) {
			return JSONResult.INVALID_PARAMETER;
		}
		JSONObject json = new JSONObject();
		StringBuffer buffer = new StringBuffer();
		Student student = null;
		JSONArray array = new JSONArray();
		ArrayList<Integer> receivers = new ArrayList<Integer>();
		for (int i = 0; i < ids.length; i++) {
			int id = Integer.parseInt(ids[i]);
			student = studentService.findStu(id);
			if (student == null) {
				array.add(id);
			} else {
				String receiver = student.getPhone();
				buffer.append(receiver);
				receivers.add(id);
				if (i != ids.length - 1)
					buffer.append(",");
				// TODO
			}
		}
		System.out.println(buffer.toString());
		if (array != null) {
			json.accumulate("FAIL_INVAILD_ID", JSONObject.fromObject(array));
		}

		Message message = new Message();
		message.setContent(content);
		message.setSender(user.getId());
		String createTime = TimeStampUtil.TimestampToString();
		message.setSendTime(createTime);
		message.setReceivers(receivers);

		messageService.addMsg(message);
		return null;
	}

	private JSONArray objectToJson(List<Message> messages, String sender) {
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		ArrayList<Integer> receivers = null;
		Student student = null;
		ArrayList<String> receivers_str = new ArrayList<String>();
		for (Message message : messages) {
			receivers = message.getReceivers();
			for (int i = 0; i < receivers.size(); i++) {
				student = studentService.findStu(receivers.get(i));
				String receiver = student.getName();
				receivers_str.add(receiver);
			}
			json.accumulate("id", message.getId());
			json.accumulate("sender", sender);
			json.accumulate("content", message.getContent());
			json.accumulate("sendTime", message.getSendTime());
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
