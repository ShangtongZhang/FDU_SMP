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
import edu.fudan.msg.pojo.Comment;
import edu.fudan.msg.pojo.Student;
import edu.fudan.msg.pojo.Tag;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.service.CommentService;
import edu.fudan.msg.service.OrganizationService;
import edu.fudan.msg.service.StudentService;
import edu.fudan.msg.util.TimeStampUtil;

public class EvaluateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager
			.getLogger(EvaluateServlet.class);

	public static final int UNVISIABLE = 1;
	private CommentService commentService;
	private OrganizationService organizationService;

	public EvaluateServlet() {
		commentService = new CommentService();
		organizationService = new OrganizationService();
	}

	@Override
	public void destroy() {
		super.destroy();
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
				json = addEvaluation(request, user);
				break;
			case RequestParameter.DELETE:
				json = removeEvaluation(request);
				break;
			case RequestParameter.RETRIEVE:
				json = showEvaluation(request, user);
				break;
			case RequestParameter.UPDATE:
				json = updateEvaluation(request, user);
				break;
			case RequestParameter.GET_STATISTIC:
				json = getStatistic(request, user);
				break;
			case RequestParameter.TAG:
				json = getTag(request);
				break;
			case RequestParameter.ADD_TAG:
				json = addTag(request);
				break;

			default:
				json = JSONResult.INVALID_PARAMETER;
				break;
			}
		}
		LOG.info(json);
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.flush();
		out.close();
	}

	private JSONObject getStatistic(HttpServletRequest request, User user) {
		List<Tag> tags = commentService.findAllTags();
		JSONObject json = JSONResult.success();
		JSONArray array = new JSONArray();
		JSONObject json_ = null;
		List<Comment> comments = null;
		for (Tag tag : tags) {
			comments = organizationService.findCommentsByTagUser(tag.getId(),
					user.getId());
			json_ = JSONObject.fromObject(tag);
			json_.accumulate("sum", comments.size());
			array.add(json_);
		}
		json.accumulate(JSONResult.DATA, array);
		return json;
	}

	private JSONObject addTag(HttpServletRequest request) {
		String tagName = request.getParameter("tagName");
		Tag tag = new Tag();
		tag.setTagName(tagName);

		int result = commentService.addTag(tag);
		if (result > 0)
			return JSONResult.success();
		else
			return JSONResult.DB_EEROR;
	}

	private JSONObject getTag(HttpServletRequest request) {
		List<Tag> tags = commentService.findAllTags();
		JSONObject json = JSONResult.success();
		JSONArray array = new JSONArray();
		for (Tag tag : tags) {
			array.add(JSONObject.fromObject(tag));
		}
		json.accumulate(JSONResult.DATA, array);
		return json;
	}

	private JSONObject removeEvaluation(HttpServletRequest request) {
		String cidStr = request.getParameter("cid");
		if (cidStr == null) {
			return JSONResult.INVALID_PARAMETER;
		}
		int cid = Integer.parseInt(cidStr);
		Comment com = commentService.findCommentById(cid);
		if (com == null) {
			return JSONResult.INVALID_PARAMETER;
		}
		boolean result = commentService.deleteComment(cid);
		if (result) {
			return JSONResult.success();
		} else
			return JSONResult.DB_EEROR;
	}

	private JSONObject showEvaluation(HttpServletRequest request, User user) {
		String sidStr = request.getParameter("sid");

		if (sidStr == null) {
			return JSONResult.INVALID_PARAMETER;
		}
		int uid = user.getId();
		int sid = Integer.parseInt(sidStr);
		List<Comment> comments = organizationService.findCommentsByStudentUser(
				sid, uid);

		if (comments == null) {
			return JSONResult.NO_DATA;
		} else {
			for (int i = 0; i < comments.size(); i++) {
				if (comments.get(i).getVisibility() == UNVISIABLE
						&& comments.get(i).getUid() != uid) {
					comments.remove(i);
				}
			}

			JSONObject json = JSONResult.success();
			JSONArray array = new JSONArray();
			for (Comment comment : comments) {
				List<Tag> tags = commentService.getTagsByComment(comment
						.getId());
				array.add(objectToJson(comment, tags));
			}
			json.accumulate(JSONResult.DATA, array);
			return json;
		}

	}

	private JSONObject updateEvaluation(HttpServletRequest request, User user) {
		String cidStr = request.getParameter("cid");
		String[] tagstrs = request.getParameterValues("tags");
		String tagStr = request.getParameter("tag");
		String period = request.getParameter("period");
		String topic = request.getParameter("topic");
		String comment = request.getParameter("comment");
		String visilibityStr = request.getParameter("visilibity");
		if (cidStr.length() == 0
				|| (tagStr.length() == 0 && period.length() == 0
						&& topic.length() == 0 && comment.length() == 0 && visilibityStr
						.length() == 0)) {
			return JSONResult.INVALID_PARAMETER;
		}
		int cid = Integer.parseInt(cidStr);
		int visilibity = Integer.parseInt(visilibityStr);
		ArrayList<Integer> tags = new ArrayList<Integer>();
		for (int i = 0; i < tagstrs.length; i++) {
			tags.add(Integer.parseInt(tagstrs[i]));
		}
		Comment com = commentService.findCommentById(cid);
		ArrayList<Integer> tids = new ArrayList<Integer>();
		for (int i = 0; i < tagstrs.length; i++) {
			tids.add(Integer.parseInt(tagstrs[i]));
		}

		String createTime = TimeStampUtil.TimestampToString();
		com.setComment(comment);
		com.setCreatedTime(createTime);
		com.setPeriod(period);
		com.setUid(user.getId());
		com.setVisibility(visilibity);
		com.setTopic(topic);
		JSONObject json = new JSONObject();
		int newcid = commentService.addComment(com, tids);

		if (newcid > 0) {
			json = JSONResult.success();
			return json;
		} else
			return JSONResult.DB_EEROR;
	}

	private JSONObject addEvaluation(HttpServletRequest request, User user) {
		String sidStr = request.getParameter("sid");
		String[] tagstrs = request.getParameterValues("tags[]");
		String period = request.getParameter("period");
		String topic = request.getParameter("topic");
		String comment = request.getParameter("comment");
		String visibilityStr = request.getParameter("visibility");
		String oidStr = request.getParameter("oid");

		if (sidStr == null || oidStr == null || period == null || topic == null
				|| comment == null || visibilityStr == null) {
			return JSONResult.INVALID_PARAMETER;
		}

		int sid = Integer.parseInt(sidStr), oid = Integer.parseInt(oidStr), visilibity = Integer
				.parseInt(visibilityStr);
		ArrayList<Integer> tids = new ArrayList<Integer>();
		for (int i = 0; i < tagstrs.length; i++) {
			tids.add(Integer.parseInt(tagstrs[i]));
		}

		String createTime = TimeStampUtil.TimestampToString();
		Comment com = new Comment();
		com.setComment(comment);
		com.setCreatedTime(createTime);
		com.setPeriod(period);
		com.setUid(user.getId());
		com.setVisibility(visilibity);
		com.setSid(sid);
		com.setOid(oid);
		com.setTopic(topic);
		JSONObject json = new JSONObject();
		int cid = commentService.addComment(com, tids);

		if (cid > 0) {

			json = JSONResult.success();
			return json;
		} else
			return JSONResult.DB_EEROR;

	}

	private static JSONObject objectToJson(Comment comment, List<Tag> tags) {
		JSONObject json = new JSONObject();
		json.accumulate("evaluation", JSONObject.fromObject(comment));
		json.accumulate("tags", JSONArray.fromObject(tags));
		return json;
	}

	private static JSONObject objectToJson(Student stu, Comment comment,
			List<Tag> tags) {
		JSONObject json = new JSONObject();
		json.accumulate("student", JSONObject.fromObject(stu));
		json.accumulate("evaluation", JSONObject.fromObject(comment));
		json.accumulate("tags", JSONArray.fromObject(tags));
		return json;
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public static void main(String[] args) {
		CommentService commentService = new CommentService();
		OrganizationService organizationService = new OrganizationService();
		StudentService studentService = new StudentService();
		// String comment = "该生表现很好。";
		// String createTime = TimeStampUtil.TimestampToString();
		// String period = "2014年11月28日2点";
		// int uid = 14;
		// int visilibity = 1;
		// int sid = 17;
		// String topic = "谈心";
		// ArrayList<Integer> tids = new ArrayList<Integer>();
		// tids.add(1);
		// tids.add(2);
		// Comment com = new Comment();
		// com.setComment(comment);
		// com.setCreatedTime(createTime);
		// com.setPeriod(period);
		// com.setUid(uid);
		// com.setVisibility(visilibity);
		// com.setSid(sid);
		// com.setTopic(topic);
		// commentService.addComment(com, tids);
		List<Comment> comments = organizationService.findCommentsByTagUser(1,
				19);
		JSONArray array = new JSONArray();
		JSONObject json = JSONResult.success();
		for (Comment comment : comments) {
			int sid = comment.getSid();
			Student stu = studentService.findStu(sid);
			List<Tag> tags = commentService.getTagsByComment(comment.getId());
			array.add(objectToJson(stu, comment, tags));
		}
		json.accumulate(JSONResult.DATA, array);
		System.out.println(json);
	}
}
