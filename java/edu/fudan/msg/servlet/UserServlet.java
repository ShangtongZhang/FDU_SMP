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
import edu.fudan.msg.constant.JSONResult;
import edu.fudan.msg.constant.RequestParameter;
import edu.fudan.msg.pojo.Organization;
import edu.fudan.msg.pojo.OrganizationUser;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.service.OrgNode;
import edu.fudan.msg.service.OrganizationService;
import edu.fudan.msg.service.UserService;
import edu.fudan.msg.util.PasswordEncryptionUtil;
import edu.fudan.msg.util.TimeStampUtil;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int UNLOGIN = 0;
	private static final int UNAUTHENTICATED = 0;
	private static final String ADMIN = "admin";

	private UserService userService;
	private OrganizationService organizationService;

	private static final int DELETE = 0;
	private static final int EXIST = 1;

	public UserServlet() {
		super();
		userService = new UserService();
		organizationService = new OrganizationService();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String type = request.getParameter("action");
		JSONObject json = null;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (type == null) {
			json = JSONResult.INVALID_PARAMETER;
		} else {
			char key = type.charAt(0);
			switch (key) {
			case RequestParameter.LOGIN:
				json = login(request);
				break; // For login
			case RequestParameter.LOGOUT:
				json = logout(request);
				break; // For logout
			case RequestParameter.REGISTER:
				json = register(request);
				break; // For register
			case RequestParameter.UPDATE:
				json = update(request, user);
				break;
			case RequestParameter.RETRIEVE:
				json = getUsers(request, user);
				break;
			case RequestParameter.SELF_INFO:
				json = getInfo(request, user);
				break;
			case RequestParameter.MANAGE:
				json = manageUser(request, user);
				break;
			case RequestParameter.GET_AUTHORITYTree:
				json = getAuthorityTree(request, user);
				break;
			case RequestParameter.GET_AUTHORITY:
				json = getAuthority(request, user);
				break;
			case RequestParameter.ASSIGN_AUTHORITY:
				json = assignAuthority(request, user);
				break;
			default:
				json = JSONResult.INVALID_PARAMETER;
				break;
			}
		}

		response.setContentType("text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(json.toString());
		out.flush();
		out.close();
	}

	private JSONObject getAuthority(HttpServletRequest request, User user) {
		JSONObject json = null;
		if (user.getRemark().equals(ADMIN)) {
			String uidStr = request.getParameter("uid");
			if (uidStr == null)
				return JSONResult.INVALID_PARAMETER;
			int uid = RequestParameter.parseInt(uidStr);
			List<Organization> orgs = organizationService.getOrgsByUser(uid);
			JSONArray array = new JSONArray();
			for (Organization org : orgs) {
				array.add(org.getId());
			}
			json = JSONResult.success();
			json.accumulate("authority", array);
			return json;
		} else
			return JSONResult.UNAUTHENTICATED_ERROR;
	}

	private JSONObject assignAuthority(HttpServletRequest request, User user) {
		JSONObject json = null;
		if (user.getRemark().equals(ADMIN)) {
			String uidStr = request.getParameter("uid");
			String[] oidStrs = request.getParameterValues("oids[]");
			if (uidStr == null)
				return JSONResult.INVALID_PARAMETER;
			int uid = RequestParameter.parseInt(uidStr);

			List<Organization> orgs = organizationService.getOrgsByUser(user
					.getId());
			ArrayList<Integer> deleted = getDifference(orgs, oidStrs, DELETE);
			for (Integer oid : deleted) {
				organizationService.deleteUserFromOrg(oid, uid);
			}
			ArrayList<Integer> exist = getDifference(orgs, oidStrs, EXIST);
			JSONArray array = new JSONArray();
			for (Integer oid : exist) {
				Organization org = organizationService.findOrg(oid);
				OrganizationUser ou = new OrganizationUser();
				String createTime = TimeStampUtil.TimestampToString();
				ou.setCreateTime(createTime);
				ou.setUid(uid);
				ou.setOid(oid);
				boolean result = organizationService.addUserToOrg(ou);
				if (!result) {
					array.add(org.getId());
				}
			}
			if (array.isEmpty())
				return JSONResult.success();
			else {
				json = JSONResult.ASSIGN_AUTHORITY_ERROR;
				json.accumulate("Fail organizations", array);
				return json;
			}
		} else {
			return JSONResult.UNAUTHENTICATED_ERROR;
		}
	}

	private ArrayList<Integer> getDifference(List<Organization> orgs,
			String[] oidStrs, int type) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		switch (type) {
		case DELETE:
			if (oidStrs == null) {
				if (orgs != null) {
					for (Organization org : orgs) {
						result.add(org.getId());
					}
				}
				return result;
			} else {
				if (orgs != null) {
					for (int i = 0; i < oidStrs.length; i++) {
						int oid = Integer.parseInt(oidStrs[i]);
						for (int j = 0; j < orgs.size(); j++) {
							if (orgs.get(j).getId() == oid) {
								orgs.remove(j);
								break;
							}
						}
					}
					for (int k = 0; k < orgs.size(); k++) {
						result.add(orgs.get(k).getId());
					}
				}
				return result;
			}
		case EXIST:
			if (oidStrs == null) {
				return result;
			} else {
				for (int i = 0; i < oidStrs.length; i++) {
					int oid = Integer.parseInt(oidStrs[i]);
					result.add(oid);
				}
				return result;
			}
		default:
			break;
		}
		return result;

	}

	private JSONObject getAuthorityTree(HttpServletRequest request, User user) {
		JSONObject json = null;
		if (user.getRemark().equals(ADMIN)) {
			OrgNode root = organizationService.getOrgTreeByUser(user.getId());
			String uidStr = request.getParameter("uid");
			if (uidStr == null)
				return JSONResult.INVALID_PARAMETER;
			int uid = RequestParameter.parseInt(uidStr);
			List<Organization> orgs = organizationService.getOrgsByUser(uid);
			JSONArray data = new JSONArray();

			data.add(objectToJson(root, orgs));
			json = JSONResult.success();
			json.accumulate(JSONResult.DATA, data);
			return json;
		} else {
			return JSONResult.UNAUTHENTICATED_ERROR;
		}

	}

	private JSONObject getInfo(HttpServletRequest request, User user) {
		int uid = user.getId();
		User cur_user = userService.findUser(uid);
		JSONObject json = JSONResult.success();
		json.accumulate("User", userToJson(cur_user, null));
		return json;
	}

	private JSONObject getUsers(HttpServletRequest request, User user) {
		JSONObject json = null;
		if (user.getRemark().equals(ADMIN)) {
			List<User> users = userService.findAllUsers();
			JSONArray array = new JSONArray();
			for (User cur : users) {
				if (cur.getId() != 19) {
					List<Organization> orgs = organizationService
							.getOrgsByUser(user.getId());
					array.add(userToJson(cur, orgs));
				}
			}
			json = JSONResult.success();
			json.accumulate("Users", array);

			return json;
		} else {
			return JSONResult.UNAUTHENTICATED_ERROR;
		}
	}

	private JSONObject manageUser(HttpServletRequest request, User user) {
		if (user.getRemark().equals(ADMIN)) {
			String uidStr = request.getParameter("uid");
			String typeStr = request.getParameter("type");

			if (uidStr == null || typeStr == null)
				return JSONResult.INVALID_PARAMETER;
			int uid = RequestParameter.parseInt(uidStr);
			int type = RequestParameter.parseInt(typeStr);
			User u = userService.findUser(uid);
			u.setIsAuthenticated(type);
			boolean result = userService.updateUser(u);

			if (result) {
				return JSONResult.success();
			} else
				return JSONResult.DB_EEROR;
		} else {
			return JSONResult.UNAUTHENTICATED_ERROR;
		}

	}

	private JSONObject update(HttpServletRequest request, User user) {
		String password = request.getParameter("password");
		String[] emails = request.getParameterValues("emails[]");
		String[] phones = request.getParameterValues("phones[]");
		String encrypted_password = PasswordEncryptionUtil.MD5(password);
		if (password == null)
			return JSONResult.INVALID_PARAMETER;

		user.setEmail(emails[0]);
		user.setPhone(phones[0]);
		ArrayList<String> email_list = new ArrayList<String>();
		ArrayList<String> phone_list = new ArrayList<String>();
		for (int i = 0; i < phones.length; i++) {
			phone_list.add(phones[i]);
		}

		for (int i = 0; i < emails.length; i++) {
			email_list.add(emails[i]);
		}
		user.setEmails(email_list);
		user.setPhones(phone_list);
		user.setPassword(encrypted_password);
		boolean result = userService.updateUser(user);
		if (result)
			return JSONResult.success();
		else
			return JSONResult.DB_EEROR;

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private JSONObject login(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String encrypted_password = PasswordEncryptionUtil.MD5(password);

		User user = userService.getUserByUsername(username);
		if (user != null && user.getIsAuthenticated() == UNAUTHENTICATED) {
			return JSONResult.UNAUTHENTICATED_ERROR;
		}
		if (user != null && user.getPassword().equals(encrypted_password)) {

			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			return JSONResult.success();
		}

		return JSONResult.LOGIN_ERROR;
	}

	private JSONObject register(HttpServletRequest request) {
		String username = request.getParameter("username");

		if (null != userService.getUserByUsername(username)) {
			return JSONResult.REGISTER_EXIST;
		}
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String ip = request.getRemoteAddr();
		String createTime = TimeStampUtil.TimestampToString();
		String encrypted_password = PasswordEncryptionUtil.MD5(password);
		User user = new User();
		user.setUsername(username);
		user.setPassword(encrypted_password);
		user.setEmail(email);
		user.setPhone(phone);
		user.setName(name);
		user.setLastIP(ip);
		user.setCreatesTime(createTime);
		user.setLoginCount(UNLOGIN);
		ArrayList<String> email_list = new ArrayList<String>();
		ArrayList<String> phone_list = new ArrayList<String>();
		email_list.add(email);
		phone_list.add(phone);
		user.setEmails(email_list);
		user.setPhones(phone_list);
		user.setIsAuthenticated(UNAUTHENTICATED);

		int result = userService.addUser(user);

		if (result > 0) {
			return JSONResult.success();
		} else {
			return JSONResult.DB_EEROR;
		}
	}

	private JSONObject logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (null != session.getAttribute("user")) {
			session.removeAttribute("user");
		}
		return JSONResult.success();
	}

	public static JSONObject userToJson(User user, List<Organization> orgs) {
		JSONObject json = new JSONObject();
		json.accumulate("username", user.getUsername());
		json.accumulate("name", user.getName());
		json.accumulate("phone", user.getPhones());
		json.accumulate("email", user.getEmails());
		json.accumulate("id", user.getId());
		json.accumulate("isAuthenticated", user.getIsAuthenticated());
		json.accumulate("createdTime", user.getCreatedTime());
		if (orgs != null) {
			JSONArray array = new JSONArray();
			for (Organization org : orgs) {
				array.add(org.getId());
			}
			json.accumulate("authority", array);
		}
		return json;
	}

	public static JSONObject objectToJson(OrgNode o, List<Organization> orgs) {
		JSONObject json = new JSONObject();
		json.accumulate("id", o.getOrganization().getId());
		json.accumulate("pid", o.getOrganization().getPid());
		json.accumulate("description", o.getOrganization().getDescription());
		json.accumulate("stucount", o.getOrganization().getStuCount());
		if (orgs != null) {
			if (orgs.size() == 0) {
				json.accumulate("text", o.getOrganization().getTitle());
			} else {
				for (int i = 0; i < orgs.size(); i++) {
					if (o.getOrganization().getId() == orgs.get(i).getId()) {
						json.accumulate("text", o.getOrganization().getTitle()
								+ "[原有]");
						break;
					}
				}
				if (!json.containsKey("text")) {
					json.accumulate("text", o.getOrganization().getTitle());
				}
			}
		} else
			json.accumulate("text", o.getOrganization().getTitle());

		JSONArray jsonArray = new JSONArray();
		for (int j = 0; j < o.getSubNodes().size(); j++) {
			jsonArray.add(objectToJson(o.getSubNodes().get(j), orgs));

		}
		if (o.getSubNodes().size() != 0) {
			json.accumulate("nodes", jsonArray);
		}

		return json;
	}

	public static void main(String[] args) {
		OrganizationService organizationService = new OrganizationService();
		List<Organization> orgs = organizationService.getOrgsByUser(44);
		String[] oidStrs = { "37", "39", "40",  "44", "46", "52" };
		for (Organization organization : orgs) {
			System.out.println(organization.getId());
		}
		UserServlet userServlet = new UserServlet();
		ArrayList<Integer> deleted = userServlet.getDifference(orgs, oidStrs,
				DELETE);
		System.out.print("deleted   ");
		for (Integer integer : deleted) {
			System.out.print("  "+integer);
		}
		System.out.println();
		ArrayList<Integer> exist = userServlet.getDifference(orgs, oidStrs, EXIST);
		System.out.print("exist   ");
		for (Integer integer : exist) {
			System.out.print("  "+integer);
		}
		System.out.println();
	}
}
