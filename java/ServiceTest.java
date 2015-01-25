import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.ResolverUtil.Test;

import edu.fudan.msg.service.*;
import edu.fudan.msg.pojo.*;

public class ServiceTest {

	public static void main(String[] args) {
		new OrgTest().test6();
		//new CommentTest().test2();
		//new StuTest().test();
		//new UserTest().test();
		//new MsgTest().test();
		//new MailTest().test();
		//new OrgTest().test();
	}

}

class MsgTest {
	public void test() {
		Message m = new Message();
		m.setContent("hello world!");
		ArrayList<Integer> rs = new ArrayList<Integer>();
		rs.add(1);
		rs.add(2);
		rs.add(3);
		m.setReceivers(rs);
		m.setSender(1);
		m.setSendTime(" ");
		m.setStatus(0);
		MessageService ms = new MessageService();
		ms.addMsg(m);
	}
}

class MailTest {
	public void test() {
		MailService ms = new MailService();
		
		Mail m = new Mail();
		m.setContent("hello world!");
		ArrayList<Integer> rs = new ArrayList<Integer>();
		rs.add(1);
		rs.add(2);
		rs.add(3);
		m.setReceivers(rs);
		m.setSender(1);
		m.setSendTime(" ");
		m.setStatus(0);
		
		ms.addMail(m);
		List<Mail> mails = ms.getMailsBySender(1);
		rs = mails.get(0).getReceivers();
	}
}

class UserTest {
	public void test() {
		UserService us = new UserService();
		//User u = us.getUserByUsername("p1");
		//int c = us.countUsername("p1");
		User u = new User();
		u.setId(36);
		u.setUsername("mike2");
		u.setPassword("123");
		u.setName("mike");
		u.setPhone("p1");
		u.setEmail("e1");
		u.setCreatesTime("now");
		u.setLoginCount(0);
		u.setIsAuthenticated(0);
		u.emails.add("e1");
		u.emails.add("e2");
		u.phones.add("p1");
		u.phones.add("p2");
		us.updateUser(u);
		//us.addUser(u);
	}
}

class StuTest {
	public void test() {
		StudentService ss = new StudentService();
		//List<Student> s = ss.getStudentsByOrg(129);
		//Student s1 = ss.getStudentByNo("adf");
	}
}

class OrgTest {
	public void initTable() {
	}
	
	public void test() {
		OrganizationService os = new OrganizationService();
		/*Organization o1 = new Organization("MATH", 1, "");
		Organization o2 = new Organization("13EE", 4, "");
		os.addOrg(o1);
		os.addOrg(o2);*/
		Organization o3 = os.findOrg(1);
		o3.setDescription("FDU University");
		os.updateOrg(o3);
	}
	
	public void test2() {
		OrganizationService os = new OrganizationService();
		OrgNode r = os.getRootOrgNode();
		OrgNode t = os.getOrgTreeByUser(19);
		OrgNode t3 = os.getOrgTreeByUser(15);
		//os.deleteUserFromOrg(9, 14);
		os.deleteUserFromOrg(4, 19);
		//os.deleteUserFromOrg(4, 19);
		//os.deleteUserFromOrg(6, 19);
		//os.deleteUserFromOrg(oid, uid);
		OrgNode t2 = os.getOrgTreeByUser(19);
	}
	
	public void test3() {
		OrganizationService os = new OrganizationService();
		Organization o = new Organization();
		o.setDescription("");
		o.setPid(6);
		o.setTitle("团委");
		o.setStuCount(0);
		os.addOrganization(o, new OrganizationUser(20,0,""));
		/*os.addUserToOrg(new OrganizationUser(19, 1, ""));
		os.addUserToOrg(new OrganizationUser(14, 4, ""));
		os.addUserToOrg(new OrganizationUser(14, 9, ""));
		os.addUserToOrg(new OrganizationUser(15, 5, ""));*/
	}
	
	public void test4() {
		OrganizationService os = new OrganizationService();
		int c = os.getStudentsCount(19, 4);
		List<Student> ss = os.getStudentsOfOrg(19 ,4, 0, c);
		
		ss = os.searchStudentsByName(19, 1, "小%");
		ss = os.searchStudentsByNo(19, 1, "004%");
	}
	
	public void test5() {
		OrganizationService os = new OrganizationService();
		//List<Comment> ss = os.findCommentsByUser(19);
		//os.deleteStuFromOrg(22, 4, 19);
		//os.getOrgTreeByUser();
		os.addStuToOrg(new OrganizationStudent(46, 162, ""));
		os.addStuToOrg(new OrganizationStudent(47, 162, ""));
		os.addStuToOrg(new OrganizationStudent(48, 161, ""));
		int c = os.getStudentsCount(19, 1);
		//os.deleteStuFromOrg(46, 159, 19);
		List<Student> ss = os.getStudentsOfOrg(19, 1, 0, 3);
		System.out.println(c);
	}
	
	public void test6() {
		OrganizationService os = new OrganizationService();
		//List<Student> ss = os.searchStudentsByName(19, 1, "");
		//List<Comment> cs = os.findCommentsByTagUser(1, 19);
		//os.addUserToOrg(new OrganizationUser(34, 1, ""));
		//OrgNode root = os.getRootOrgNode();
		//ArrayList<OrgNode> orgns = new ArrayList<OrgNode>();
		//os.getAllSubNodes(root, orgns);
		//List<Organization> orgs = os.getOrgsByUser(19);
		os.findCommentsByStudentUser(7415, 43);
	}
}

class CommentTest {
	
	public void test() {
		CommentService cs = new CommentService();
		Comment cm1 = new Comment(2, 3, 1, "good", "1h", "hello world", "today", 1);
		Comment cm2 = new Comment(4, 7, 1, "bad", "1h", "hello world", "today", 1);
		
		//int id1 = cs.addComment(cm1);
		//int id2 = cs.addComment(cm2);
		
		//System.out.println(id1);
		//System.out.println(id2);
		
		cs.deleteComment(1);
		
		cm1.setSid(5);
		
		cs.updateComment(cm1);
		
		cm1 = cs.findCommentById(2);
		
		//List<Comment> cms = cs.findAllComments();
		
		//cms = cs.findCommentsByStudentId(4);
		
		//cms = cs.findCommentsByUserId(9);
	}
	
	public void test2() {
		CommentService cs = new CommentService();
		//List<Comment> cm1 = cs.findCommentsByUser(19);
		//List<Comment> cm2 = cs.searchCommentsByContentUser(19, "");
		cs.addTag(new Tag("炉石", 888));
	}
}
