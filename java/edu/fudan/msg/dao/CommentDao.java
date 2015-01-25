package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.Comment;

public interface CommentDao extends GenericDao<Comment>{
	
	public List<Comment> findCommentsByTagUser(int tid, int uid);
	
	public List<Comment> findCommentsByStudentUser(int sid, int uid);
	
	public List<Comment> findCommentsByUser(int uid);
	
	public List<Comment> findCommentsByStudentUserTag(int sid, int uid, int tid);
	
	public List<Comment> searchCommentsByContentSid(int sid, String contPattern);
	
	public boolean deleteBySidUid(int sid, int uid);
	
	public List<Comment> findCommentsBySidTid(int sid, int tid);
	
	public List<Comment> findCommentsBySid(int sid);

	public List<Comment> findCommentsByTidOid(int tid, int oid);
	
	public List<Comment> findCommentsBySidOid(int sid, int oid);
	
	public List<Comment> searchCommentsByContentOid(String contPattern, int oid);
	
	public List<Comment> findCommentsByStuAndOrgsOfUser(int sid, int uid);
	
	public List<Comment> findCommentsByTagAndOrgsOfUser(int tid, int uid);
	
	public List<Comment> searchCommentsByContentAndOrgsOfUser(String contPattern, int uid);
}
