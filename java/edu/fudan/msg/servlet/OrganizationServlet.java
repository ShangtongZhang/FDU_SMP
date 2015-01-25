package edu.fudan.msg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import edu.fudan.msg.pojo.Organization;
import edu.fudan.msg.pojo.OrganizationUser;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.service.OrgNode;
import edu.fudan.msg.service.OrganizationService;
import edu.fudan.msg.util.TimeStampUtil;

public class OrganizationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager
			.getLogger(OrganizationServlet.class);

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
	private OrganizationService organizationService;

	/**
	 * Constructor of the object.
	 */
	public OrganizationServlet() {
		super();
		organizationService = new OrganizationService();

	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/json;charset-utf-8");
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
				json = add(request, user);
				break;
			case RequestParameter.UPDATE:
				json = update(request, user);
				break;
			case RequestParameter.DELETE:
				json = delete(request, user);
				break;
			case RequestParameter.RETRIEVE:
				// json = list(request, user);
				json = list(user);
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

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private JSONObject list(User user) {
		// private JSONObject list(HttpServletRequest request, User user) {

		OrgNode orgNode = organizationService.getOrgTreeByUser(user.getId());

		JSONArray data = new JSONArray();

		JSONObject ret = objectToJson(orgNode);
		if (null != ret) {
			data.add(ret);
		}

		JSONObject json = JSONResult.success();
		json.accumulate(JSONResult.DATA, data);
		// System.out.println(json);
		return json;
	}

	private JSONObject add(HttpServletRequest request, User user) {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String pidStr = request.getParameter("pId");
		int pid = RequestParameter.parseInt(pidStr);

		if (null == title || pid < 0) {
			return JSONResult.INVALID_PARAMETER;
		}

		JSONObject json = null;
		Organization org = new Organization();
		org.setDescription(description);
		org.setPid(pid);
		org.setTitle(title);
		OrganizationUser ou = new OrganizationUser();
		String createTime = TimeStampUtil.TimestampToString();
		ou.setCreateTime(createTime);
		ou.setUid(user.getId());

		int id = organizationService.addOrganization(org, ou);

		if (id < 0) {
			json = JSONResult.DB_EEROR;
		} else {
			json = JSONResult.success();
			json.accumulate("id", id);
			return json;
		}
		return json;
	}

	private JSONObject update(HttpServletRequest request, User user) {
		String idStr = request.getParameter("id");
		String newTitle = request.getParameter("newTitle");
		String newDescription = request.getParameter("newDescription");
		int id = RequestParameter.parseInt(idStr);
		Organization org = organizationService.findOrg(id);

		if (null == idStr || null == newTitle || newDescription == null
				|| null == org) {
			return JSONResult.INVALID_PARAMETER;
		}
		if (newTitle != null)
			org.setTitle(newTitle);
		if (newDescription != null)
			org.setDescription(newDescription);
		boolean result = organizationService.updateOrg(org);

		if (result) {
			// json.accumulate(JSONResult.DATA, objectToJson(c));
			return JSONResult.success();
		} else {
			return JSONResult.DB_EEROR;
		}
	}

	private JSONObject delete(HttpServletRequest request, User user) {
		String idStr = request.getParameter("id");
		int id = RequestParameter.parseInt(idStr);
		if (id == 1) {
			return JSONResult.INVALID_PARAMETER;
		}
		Organization org = organizationService.findOrg(id);
		if (null == org) {
			return JSONResult.INVALID_PARAMETER;
		}
		List<Organization> orgs = organizationService.getOrgsByUser(user
				.getId());
		boolean hasAuth = false;
		for (Organization organization : orgs) {
			if (organization.getId() == id) {
				hasAuth = true;
			}
		}
		if (!hasAuth) {
			return JSONResult.UNAUTHENTICATED_ERROR;
		}
		if (organizationService.deleteOrg(id)) {
			return JSONResult.success();
		} else {
			return JSONResult.DB_EEROR;
		}
	}

	private JSONObject objectToJson(OrgNode o) {
		if (null == o) {
			return null;
		}
		JSONObject json = new JSONObject();
		json.accumulate("text", o.getOrganization().getTitle());
		json.accumulate("id", o.getOrganization().getId());
		json.accumulate("pid", o.getOrganization().getPid());
		json.accumulate("description", o.getOrganization().getDescription());
		json.accumulate("stucount", o.getOrganization().getStuCount());
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < o.getSubNodes().size(); i++) {
			jsonArray.add(objectToJson(o.getSubNodes().get(i)));
		}
		if (o.getSubNodes().size() != 0) {
			json.accumulate("nodes", jsonArray);
		}
		return json;
	}

}
