package edu.fudan.msg.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.fudan.msg.dao.CommentDao;
import edu.fudan.msg.dao.OrgStuDao;
import edu.fudan.msg.dao.OrgUserDao;
import edu.fudan.msg.dao.OrganizationDao;
import edu.fudan.msg.dao.StudentDao;
import edu.fudan.msg.dao.impl.CommentDaoImpl;
import edu.fudan.msg.dao.impl.OrgStuDaoImpl;
import edu.fudan.msg.dao.impl.OrgUserDaoImpl;
import edu.fudan.msg.dao.impl.OrganizationDaoImpl;
import edu.fudan.msg.dao.impl.StudentDaoImpl;
import edu.fudan.msg.pojo.Comment;
import edu.fudan.msg.pojo.Entry;
import edu.fudan.msg.pojo.Organization;
import edu.fudan.msg.pojo.OrganizationStudent;
import edu.fudan.msg.pojo.OrganizationUser;
import edu.fudan.msg.pojo.Student;

@SuppressWarnings("unchecked")
public class OrganizationService {

	private OrgNode rootOrgNode;
	
	//这个变量我已经不记得是干嘛的了，最好不要动它。
	private ArrayList<OrgNode> nodes = new ArrayList<OrgNode>();
	
	private OrganizationDao orgDao = new OrganizationDaoImpl();
	private OrgUserDao orgUserDao = new OrgUserDaoImpl();
	private OrgStuDao orgStuDao = new OrgStuDaoImpl();
	private StudentDao stuDao = new StudentDaoImpl();
	private CommentDao cmtDao = new CommentDaoImpl();

	// 默认构造函数
	public OrganizationService() {
		updateRootOrgNode();
	}
	
	public List<Comment> findCommentsByTagUser(int tid, int uid) {
		
		List<Comment> all_cmts = cmtDao.findCommentsByTagAndOrgsOfUser(tid, uid);
		/*List<Organization> accessible_orgs = getOrgsByUser(uid);
		List<Comment> all_cmts = new ArrayList<Comment>();
		for (Organization org : accessible_orgs) {
			all_cmts.addAll(cmtDao.findCommentsByTidOid(tid, org.getId()));
		}*/
		return (List<Comment>)Entry.deDuplicates(all_cmts);
	}
	
	public List<Comment> findCommentsByStudentUser(int sid, int uid) {
		List<Comment> all_cmts = cmtDao.findCommentsByStuAndOrgsOfUser(sid, uid);
		/*List<Organization> accessible_orgs = getOrgsByUser(uid);
		List<Comment> all_cmts = new ArrayList<Comment>();
		for (Organization org : accessible_orgs) {
			all_cmts.addAll(cmtDao.findCommentsBySidOid(sid, org.getId()));
		}*/
		return (List<Comment>)Entry.deDuplicates(all_cmts);
	}
	
	public List<Comment> searchCommentsByContentUser(int uid, String contPattern) {
		contPattern = preSearchPattern(contPattern);
		if (contPattern == null) {
			return new ArrayList<Comment>();
		}
		List<Comment> all_cmts = cmtDao.searchCommentsByContentAndOrgsOfUser(contPattern, uid);
		/*List<Organization> accessible_orgs = getOrgsByUser(uid);
		List<Comment> all_cmts = new ArrayList<Comment>();
		for (Organization org : accessible_orgs) {
			all_cmts.addAll(cmtDao.searchCommentsByContentOid(contPattern, org.getId()));
		}*/
		
		return (List<Comment>)Entry.deDuplicates(all_cmts);
	}
	
	
	//uid暂时没用
	public List<Student> searchStudentsByName(int uid, int oid, String namePattern) {
		namePattern = preSearchPattern(namePattern);
		if (namePattern == null) {
			return null;
		}
		return stuDao.searchStudentsByName(oid, namePattern);
	}
	
	//uid暂时没用
	public List<Student> searchStudentsByNo(int uid, int oid, String noPattern) {
		noPattern = preSearchPattern(noPattern);
		if (noPattern == null) {
			return null;
		}
		return stuDao.searchStudentsByNo(oid, noPattern);
	}
	
	//uid暂时没用
	public int getStudentsCount(int uid, int oid) {
		
		return stuDao.countStudentsByOrg(oid);
	}
	
	//start从0开始
	//uid暂时没用
	public List<Student> getStudentsOfOrg(int uid, int oid, int start, int length) {
		
		return stuDao.getStudentsByOrg(oid, start, length);
		
	}

	
	//将一个学生加添到一个org里，向上递归添加
	public boolean addStuToOrg(OrganizationStudent os) {
		
		OrgNode curNode = getOrgNode(rootOrgNode, os.getOid());
		
		if (curNode == null) {
			updateRootOrgNode();
			curNode = getOrgNode(rootOrgNode, os.getOid());
		}
		
		
		while (curNode.getPid() != -1) {
			if (orgStuDao.findBySidOid(os.getSid(), curNode.getId()) == null) {
				orgStuDao.addOne(new OrganizationStudent(os.getSid(), curNode.getId(), os.getCreatedTime()));	
			}
			curNode = curNode.getParent();
		}
		
		if (curNode.getPid() == -1) {
			if (orgStuDao.findBySidOid(os.getSid(), curNode.getId()) == null) {
				orgStuDao.addOne(new OrganizationStudent(os.getSid(), curNode.getId(), os.getCreatedTime()));
				curNode = curNode.getParent();
			}
		}
		
		return true;
	}
	
	//把一个学生从一个org里删除，向下递归删除
	public boolean deleteStuFromOrg(int sid, int oid, int uid) {
		
		OrgNode cur_node = getOrgNode(rootOrgNode, oid);
		ArrayList<OrgNode> allSubNodes = new ArrayList<OrgNode>();
		getAllSubNodes(cur_node, allSubNodes);
		allSubNodes.add(cur_node);
		
		for (OrgNode o : allSubNodes) {
			orgStuDao.deleteBySidOid(sid, o.getId());
		}
		
		if (!cmtDao.deleteBySidUid(sid, uid)) {
			return false;
		}
		return true;
	}
	
	
	//删除一个org,向下递归删除
	public boolean deleteOrg(int oid) {
		ArrayList<OrgNode> allSubNodes = new ArrayList<OrgNode>();
		OrgNode cur_node = getOrgNode(rootOrgNode, oid);
		getAllSubNodes(cur_node, allSubNodes);
		allSubNodes.add(cur_node);
		for (OrgNode n : allSubNodes) {
			orgDao.deleteOne(n.getId());
		}
		updateRootOrgNode();
		return true;
	}
	
	//把一个user从一个org移除，向下递归删除
	public boolean deleteUserFromOrg(int oid, int uid) {
		ArrayList<OrgNode> allSubNodes = new ArrayList<OrgNode>();
		OrgNode cur_node = getOrgNode(rootOrgNode, oid);
		getAllSubNodes(cur_node, allSubNodes);
		allSubNodes.add(cur_node);
		for (OrgNode n : allSubNodes) {
			orgUserDao.deleteByOidUid(n.getId(), uid);
		}
		return true;
	}

	//新建一个org，并把一个user添加到这个org,同时拥有这个org的上级节点的user自动拥有这个org
	public int addOrganization(Organization org, OrganizationUser ou) {
		int oid = orgDao.addOne(org);
		if (oid != -1) {
			updateRootOrgNode();
		} else {
			return oid;
		}
		ou.setOid(oid);
		
		OrgNode cur_node = getOrgNode(rootOrgNode, ou.getOid());

		OrgNode p_node = cur_node;
		while (p_node != null) {
			List<OrganizationUser> p_ou = orgUserDao.findByOid(p_node.getId());
			for (OrganizationUser t : p_ou) {
				if (orgUserDao.findByOidUid(oid, t.getUid()) == null) {
					orgUserDao.addOne(new OrganizationUser(t.getUid(), oid, ou.getCreateTime()));
				}
			}
			p_node = p_node.getParent();
		}
		
		updateRootOrgNode();
		
		return oid;
	}
	
	public boolean updateOrg(Organization org) {
		boolean r = orgDao.updateOne(org);
		if (r) {
			updateRootOrgNode();
		}
		return r;
	}

	public Organization findOrg(int id) {
		return orgDao.findOne(id);
	}

	public List<Organization> findAllOrgs() {
		return orgDao.findAll();
	}
	
	//返回一个user拥有的所有org
	public List<Organization> getOrgsByUser(int uid) {
		return orgDao.getOrgsByUser(uid);
	}
	
	//得到一个user的org树，已经删除nodisplayed的节点
	public OrgNode getOrgTreeByUser(int uid) {
		
		updateRootOrgNode();
		OrgNode rtOrgNode = (OrgNode)OrgNode.deepClone(rootOrgNode);
		
		List<Organization> rawOrgs = orgDao.getOrgsByUser(uid);
		if (rawOrgs.isEmpty()) {
			return null;
		}
		ArrayList<OrgNode> privilegedNodes = new ArrayList<OrgNode>();
		for (Organization o : rawOrgs) {
			privilegedNodes.add(getOrgNode(rootOrgNode, o.getId()));
		}
		
		
		Stack<OrgNode> path = new Stack<OrgNode>();
		for (OrgNode n : privilegedNodes) {
			path.clear();
			colorNode(n.getId(), rtOrgNode, path);
		}
		
		cleanNodes(rtOrgNode);
		
		return rtOrgNode;
	}
	
	// 判断cOrgNode是不是pOrgNode的直接或间接子节点,需要保证两个节点来自同一棵树
	public boolean isSubNode(OrgNode pOrgNode, OrgNode cOrgNode) {
		if (cOrgNode.getPid() == pOrgNode.getId()) {
			return true;
		}
		if (cOrgNode.getId() == pOrgNode.getId()) {
			return false;
		}
		for (OrgNode n : pOrgNode.getSubNodes()) {
			boolean r = isSubNode(n, cOrgNode);
			if (r) {
				return r;
			}
		}
		return false;
	}

	//从一颗org树中获取一个oid对应的节点
	public OrgNode getOrgNode(OrgNode rootNode, int oid) {
		if (rootNode.getId() == oid) {
			return rootNode;
		}
		for (OrgNode n : rootNode.getSubNodes()) {
			OrgNode t = getOrgNode(n, oid);
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	//获取总的org树，谨慎使用！
	public OrgNode getRootOrgNode() {
		return (OrgNode)OrgNode.deepClone(rootOrgNode);
	}
	
	/*@SuppressWarnings("unused")
	private int addOrg(Organization org) {
		int r = orgDao.addOne(org);
		if (r != -1) {
			updateRootOrgNode();
		}
		return r;
	}*/
	
	//把一个user添加到一个org，向下递归添加
	public boolean addUserToOrg(OrganizationUser ou) {
		ArrayList<OrgNode> allSubNodes = new ArrayList<OrgNode>();
		OrgNode cur_node = getOrgNode(rootOrgNode, ou.getOid());
		getAllSubNodes(cur_node, allSubNodes);
		allSubNodes.add(cur_node);
		for (OrgNode n : allSubNodes) {
			if (orgUserDao.findByOidUid(n.getId(), ou.getUid()) == null) {
				orgUserDao.addOne(new OrganizationUser(ou.getUid(), n.getId(), ou.getCreateTime()));
			}
		}
		return true;
	}
	
	//把一个user添加到一个org，不递归添加
	public boolean addUserOrg(OrganizationUser ou) {
		if (orgUserDao.findByOidUid(ou.getOid(), ou.getUid()) != null) {
			return false;
		}
		orgUserDao.addOne(ou);
		return true;
	}
	
	//已经记不清楚这个函数的用处了。。最好不要动它。。
	private void addChild(OrgNode root) {
		for (OrgNode n : nodes) {
			if (n.getPid() == root.getId()) {
				root.addChild(n);
			}
		}

		for (OrgNode n : root.getSubNodes()) {
			nodes.remove(n);
		}

		if (nodes.isEmpty()) {
			return;
		}

		for (OrgNode n : root.getSubNodes()) {
			addChild(n);
		}
	}

	//获取一个节点所有的子节点，并不包括节点本身
	private void getAllSubNodes(OrgNode cur_node, ArrayList<OrgNode> allSubNodes) {
		for (OrgNode n : cur_node.getSubNodes()) {
			allSubNodes.add(n);
			getAllSubNodes(n, allSubNodes);
		}
	}
	
	// 获得与一个user相关的org，在返回的ArrayList<OrgNode>中，不会出现一个节点是另一个节点的子节点
	/*private ArrayList<OrgNode> getOrgsByUser_(OrgNode rootOrgNode, int uid) {
		List<Organization> rawOrgs = orgDao.getOrgsByUser(uid);
		if (rawOrgs == null) {
			return null;
		}
		ArrayList<OrgNode> rawOrgNodes = new ArrayList<OrgNode>();
		ArrayList<OrgNode> rtOrgNodes = new ArrayList<OrgNode>();
		for (Organization o : rawOrgs) {
			rawOrgNodes.add(getOrgNode(rootOrgNode, o.getId()));
		}

		for (OrgNode n1 : rawOrgNodes) {
			boolean haveParent = false;
			for (OrgNode n2 : rawOrgNodes) {
				if (isSubNode(n2, n1)) {
					haveParent = true;
					break;
				}
			}
			if (!haveParent) {
				rtOrgNodes.add(n1);
			}
		}
		return rtOrgNodes;
	}*/
	
	//清除一个树中nodisplayed的节点
	//返回值表示调用函数后cur_node是否被删除
	private boolean cleanNodes(OrgNode cur_node) {
		
		if (cur_node.getColor() == OrgNode.NONDISPLAYED) {
			if (cur_node.getParent() != null) {
				cur_node.getParent().removeChild(cur_node);
			}
			return true;
		}
		
		for (int i = 0; i < cur_node.getSubNodes().size(); ++i) {
			if (cleanNodes(cur_node.getSubNodes().get(i))) {
				i--;
			}
		}
		
		return false;
		
	}
	
	//给一个user的orgtree染色
	private boolean colorNode(int oid, OrgNode cur_node, Stack<OrgNode> path) {
		if (cur_node.getId() == oid) {
			while (!path.empty()) {
				OrgNode pNode = path.pop();
				if (pNode.getColor() == OrgNode.NONDISPLAYED) {
					pNode.setColor(OrgNode.DISPLAYED);
				}
			}
			cur_node.setColor(OrgNode.PRIVILEGED);
			return true;
		}
		
		path.push(cur_node);
		
		for (OrgNode n : cur_node.getSubNodes()) {
			if (colorNode(oid, n, path)) {
				return true;
			}
		}
		
		path.pop();
		
		return false;
	}


	//更新总的org树，如果操作节点后出现null异常可以尝试调用这个方法
	private void updateRootOrgNode() {
		List<Organization> orgs = orgDao.findAll();
		nodes.clear();
		for (Organization org : orgs) {
			if (org.getPid() == -1) {
				rootOrgNode = new OrgNode(org);
				continue;
			}
			nodes.add(new OrgNode(org));
		}
		addChild(rootOrgNode);
	}
	
	//预处理搜索字符串
	private String preSearchPattern(String pattern) {
		if (pattern.contains(" ")) {
			return null;
		}
		if (pattern.startsWith("%") || pattern.endsWith("%")) {
			return pattern;
		} else {
			pattern = '%' + pattern + '%';
			return pattern;
		}
	}
	
	/*private boolean isAccessibleOrg(ArrayList<OrgNode> accessibleOrgs, OrgNode org) {
		for (OrgNode n : accessibleOrgs) {
			if (n.getId() == org.getId() || isSubNode(n, org)) {
				return true;
			}
		}
		return false;
	}*/

}
