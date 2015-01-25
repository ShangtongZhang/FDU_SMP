package edu.fudan.msg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import edu.fudan.msg.pojo.Organization;
import edu.fudan.msg.pojo.OrganizationStudent;
import edu.fudan.msg.pojo.Student;
import edu.fudan.msg.pojo.Tag;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.service.CommentService;
import edu.fudan.msg.service.OrganizationService;
import edu.fudan.msg.service.StudentService;
import edu.fudan.msg.util.TimeStampUtil;

public class StudentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager
			.getLogger(StudentServlet.class);

	private static final int SEARCH_BY_NAME = 1;
	private static final int SEARCH_BY_NO = 2;
	private static final int SEARCH_BY_COMMENT_TAG = 3;
	private static final int SEARCH_BY_COMMENT_CONTENT = 4;

	private static int PAGESIZE = 20;

	private StudentService studentService;
	private OrganizationService organizationService;
	private CommentService commentService;

	public StudentServlet() {
		super();
		commentService = new CommentService();
		studentService = new StudentService();
		organizationService = new OrganizationService();

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
				json = addStudent(request);
				break;
			case RequestParameter.DELETE:
				json = removeStudent(request, user);
				break;
			case RequestParameter.RETRIEVE:
				json = listStudent(request, user);
				break;
			case RequestParameter.UPDATE:
				json = updateStudent(request);
				break;
			case RequestParameter.ADD_TO_ORGANIZATION:
				json = addToOrganization(request);
				break;
			case RequestParameter.TAG:
				json = getTag(request);
				break;
			case RequestParameter.SEARCH_COMMENT:
				json = searchEvaluation(request, user);
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

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
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

	private JSONObject addStudent(HttpServletRequest request) {
		try {
			String studentNo = request.getParameter("studentNo").trim();
			String name = request.getParameter("name").trim();
			String phone = request.getParameter("phone").trim();
			String email = request.getParameter("email").trim();
			String remark = request.getParameter("remark").trim();
			String oidStr = request.getParameter("oid");
			Student stu = studentService.getStudentByNo(studentNo);
			int id = 0;
			int oid = 0;
			if (oidStr == null) {
				oid = 1;
			} else {
				oid = Integer.parseInt(oidStr);
			}
			OrganizationStudent organizationStudent = new OrganizationStudent();
			String creatTime = TimeStampUtil.TimestampToString();
			organizationStudent.setCreatedTime(creatTime);
			organizationStudent.setOid(oid);
			if (studentNo.length() > 0 && name.length() > 0
					&& phone.length() > 0 && email.length() > 0) {
				if (stu == null) {
					stu = new Student();
					stu.setEmail(email);
					stu.setName(name);
					stu.setPhone(phone);
					stu.setRemark(remark);
					stu.setStudentNo(studentNo);

					id = studentService.addStu(stu);
				} else {
					id = stu.getId();
				}
				if (id > 0) {
					organizationStudent.setSid(id);
					boolean result = organizationService
							.addStuToOrg(organizationStudent);
					if (result) {
						JSONObject json = JSONResult.success();
						json.accumulate(JSONResult.RECORD, id);
						return json;
					}
				} else {
					return JSONResult.DB_EEROR;
				}
			}
		} catch (Exception e) {
			LOG.error("ADD Student Exception: ", e);
		}
		return JSONResult.INVALID_PARAMETER;
	}

	private JSONObject listStudent(HttpServletRequest request, User user) {

		String oidStr = request.getParameter("oid");
		String pageStr = request.getParameter("page");
		String typeStr = request.getParameter("type");
		String numPerPageStr = request.getParameter("numPerPage");
		String searchContent = request.getParameter("search");

		if (oidStr == null || pageStr == null || numPerPageStr == null) {
			return JSONResult.INVALID_PARAMETER;
		}
		int oid = RequestParameter.parseInt(oidStr), page = RequestParameter
				.parseInt(pageStr), numPerPage = RequestParameter
				.parseInt(numPerPageStr);

		List<Organization> orgs = organizationService.getOrgsByUser(user
				.getId());
		boolean authority = false;
		for (Organization organization : orgs) {
			if (organization.getId() == oid) {
				authority = true;
				break;
			}
		}
		if (!authority)
			return JSONResult.UNAUTHENTICATED_ERROR;

		if (oid < 0 || page <= 0)
			return JSONResult.INVALID_PARAMETER;
		PAGESIZE = numPerPage;
		int sum = organizationService.getStudentsCount(user.getId(), oid);
		int page_sum = (int) Math.ceil(sum * 1.0 / PAGESIZE);
		if (sum == 0)
			return JSONResult.NO_DATA;

		if (page > page_sum)
			return JSONResult.INVALID_PARAMETER;

		JSONObject json = new JSONObject();

		List<Student> students = null;

		if (searchContent.length() == 0) {
			int start = (page - 1) * PAGESIZE;
			int length = PAGESIZE;
			if (start + PAGESIZE > sum)
				length = sum - start;
			students = organizationService.getStudentsOfOrg(user.getId(), oid,
					start, length);
		} else {
			int type = RequestParameter.parseInt(typeStr);
			switch (type) {
			case SEARCH_BY_NAME:
				students = organizationService.searchStudentsByName(
						user.getId(), oid, searchContent);
				break;
			case SEARCH_BY_NO:
				students = organizationService.searchStudentsByNo(user.getId(),
						oid, searchContent);
				break;
			default:
				json = JSONResult.INVALID_PARAMETER;
				break;
			}
			if (students == null || students.size() == 0)
				return JSONResult.NO_DATA;

			/**
			 * page check
			 */
			sum = students.size();
			int bid = (page - 1) * PAGESIZE, eid = bid + PAGESIZE;

			if (sum < eid)
				eid = sum;

			students = students.subList(bid, eid);
			page_sum = (int) Math.ceil(sum * 1.0 / PAGESIZE);
		}

		json = JSONResult.success();
		json.accumulate(JSONResult.TOTAL_PAGE_NUM, page_sum);
		json.accumulate(JSONResult.SUM, sum);
		json.accumulate(JSONResult.STUDENTS, JSONArray.fromObject(students));
		return json;
	}

	private JSONObject updateStudent(HttpServletRequest request) {
		try {
			String idStr = request.getParameter("id");
			String studentNo = request.getParameter("studentNo").trim();
			String name = request.getParameter("name").trim();
			String phone = request.getParameter("phone").trim();
			String email = request.getParameter("email").trim();
			String remark = request.getParameter("remark").trim();
			if (idStr.length() == 0 || studentNo.length() == 0
					|| name.length() == 0 || phone.length() == 0
					|| email.length() == 0) {
				return JSONResult.INVALID_PARAMETER;
			}

			int id = RequestParameter.parseInt(idStr);

			Student st = studentService.findStu(id);
			if (null != st) {
				st.setStudentNo(studentNo);
				st.setName(name);
				st.setPhone(phone);
				st.setEmail(email);
				st.setRemark(remark);

				if (studentService.updateStu(st)) {
					JSONObject json = JSONResult.success();
					json.accumulate(JSONResult.STUDENT, st);
					return json;
				} else {
					return JSONResult.DB_EEROR;
				}

			}

		} catch (Exception e) {
			LOG.error("ADD Student Exception: ", e);
		}
		return JSONResult.INVALID_PARAMETER;
	}

	private JSONObject searchEvaluation(HttpServletRequest request, User user) {
		String pageStr = request.getParameter("page");
		String typeStr = request.getParameter("type");
		String searchContent = request.getParameter("search");

		if (pageStr == null && typeStr == null && searchContent == null) {
			return JSONResult.INVALID_PARAMETER;
		}

		List<Comment> comments = null;
		int uid = user.getId();
		int type = Integer.parseInt(typeStr);
		int page = RequestParameter.parseInt(pageStr);
		JSONObject json = null;
		switch (type) {
		case SEARCH_BY_COMMENT_TAG:
			int tid = Integer.parseInt(searchContent);
			comments = organizationService.findCommentsByTagUser(tid, uid);
			break;
		case SEARCH_BY_COMMENT_CONTENT:
			comments = organizationService.searchCommentsByContentUser(uid,
					searchContent);
			break;
		default:
			json = JSONResult.INVALID_PARAMETER;
			break;
		}
		if (comments == null || comments.size() == 0)
			return JSONResult.NO_DATA;
		else {
			int sum = comments.size();
			int page_sum = (int) Math.ceil(sum * 1.0 / PAGESIZE);
			if (page > page_sum) {
				return JSONResult.INVALID_PARAMETER;
			}
			int bid = (page - 1) * PAGESIZE, eid = bid + PAGESIZE;
			if (sum < eid) {
				eid = sum;
			}
			comments = comments.subList(bid, eid);
			JSONArray array = new JSONArray();
			json = JSONResult.success();
			for (Comment comment : comments) {
				int sid = comment.getSid();
				Student stu = studentService.findStu(sid);
				List<Tag> tags = commentService.getTagsByComment(comment
						.getId());
				array.add(objectToJson(stu, comment, tags));
			}
			json = JSONResult.success();
			json.accumulate(JSONResult.TOTAL_PAGE_NUM, page_sum);
			json.accumulate(JSONResult.SUM, sum);
			json.accumulate(JSONResult.DATA, array);
		}
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

	private JSONObject removeStudent(HttpServletRequest request, User user) {
		String sidstr = request.getParameter("sid");
		String oidstr = request.getParameter("oid");
		if (sidstr == null || oidstr == null)
			return JSONResult.INVALID_PARAMETER;
		int sid = RequestParameter.parseInt(sidstr), oid = Integer
				.parseInt(oidstr);
		if (sid < 0 || oid < 0) {
			return JSONResult.INVALID_PARAMETER;
		}

		boolean result = organizationService.deleteStuFromOrg(sid, oid,
				user.getId());

		if (result) {
			return JSONResult.success();
		} else {
			return JSONResult.DB_EEROR;
		}
	}

	private JSONObject addToOrganization(HttpServletRequest request) {
		String oidStr = request.getParameter("noid");
		String[] ids = request.getParameterValues("sida[]");
		if (null == oidStr || null == ids) {
			return JSONResult.INVALID_PARAMETER;
		}

		int oid = RequestParameter.parseInt(oidStr);
		int[] sids = RequestParameter.parseInts(ids);
		Organization org = organizationService.findOrg(oid);
		if (org == null) {
			return JSONResult.INVALID_PARAMETER;
		}
		OrganizationStudent organizationStudent = new OrganizationStudent();
		String creatTime = TimeStampUtil.TimestampToString();
		organizationStudent.setCreatedTime(creatTime);
		organizationStudent.setOid(oid);
		JSONArray jsonarry = new JSONArray();
		for (int i = 0; i < sids.length; i++) {
			organizationStudent.setSid(sids[i]);
			boolean result = organizationService
					.addStuToOrg(organizationStudent);
			if (!result) {
				jsonarry.add(sids[i]);
			}
		}
		JSONObject json = new JSONObject();

		if (jsonarry.isEmpty()) {
			return JSONResult.success();
		} else {
			json.accumulate("FAIL", jsonarry);
			json.accumulate(JSONResult.RESULT, JSONResult.ERROR);
			return json;
		}

	}

}
