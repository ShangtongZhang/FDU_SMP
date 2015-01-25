package edu.fudan.msg.service;
import java.util.ArrayList;
import java.util.List;

import edu.fudan.msg.dao.CommentDao;
import edu.fudan.msg.dao.CommentTagDao;
import edu.fudan.msg.dao.TagDao;
import edu.fudan.msg.dao.impl.CommentDaoImpl;
import edu.fudan.msg.dao.impl.CommentTagDaoImpl;
import edu.fudan.msg.dao.impl.TagDaoImpl;
import edu.fudan.msg.pojo.Comment;
import edu.fudan.msg.pojo.CommentTag;
import edu.fudan.msg.pojo.Tag;


public class CommentService {
	
	private CommentDao commentDao = new CommentDaoImpl();
	private TagDao tagDao = new TagDaoImpl();
	private CommentTagDao commentTagDao = new CommentTagDaoImpl();
	
	public CommentService() {
	}
	
	//增加一个tag
	public int addTag(Tag t) {
		return tagDao.addOne(t);
	}
	
	//删除一个tag
	public boolean deleteTag(int tid) {
		return tagDao.deleteOne(tid);
	}
	
	//更新一个tag
	public boolean updateTag(Tag t) {
		return tagDao.updateOne(t);
	}
	
	//查找一个tag
	public Tag findTag(int tid) {
		return tagDao.findOne(tid);
	}
	
	//列出所有可用tag
	public List<Tag> findAllTags() {
		return tagDao.findAll();
	}
	
	//增加一条评论
	public int addComment(Comment cm, ArrayList<Integer> tids) {
		int cid = commentDao.addOne(cm);
		if (cid < 0) {
			return -1;
		}
		for (Integer tid : tids) {
			commentTagDao.addOne(new CommentTag(cid, tid));
		}
		return cid;
	}
	
	//删除一条评论(不须要在调用deleteCommentTag)
	public boolean deleteComment(int id) {
		return commentDao.deleteOne(id);
	}
	
	//更新一条评论
	public boolean updateComment(Comment cm) {
		return commentDao.updateOne(cm);
	}
	
	//为一个评论添加一个标签
	public int addCommentTag(CommentTag ct) {
		return commentTagDao.addOne(ct);
	}
	
	//为一个评论删除一个标签
	public boolean deleteCommentTag(int id) {
		return commentTagDao.deleteOne(id);
	}
	
	//取回一个评论
	public Comment findCommentById(int id) {
		return commentDao.findOne(id);
	}

	//取回一个comment对应的tag
	public List<Tag> getTagsByComment(int cid) {
		return tagDao.getTagsByComment(cid);
	}
	
	
	@SuppressWarnings("unused")
	private CommentTag findCommentTag(int id) {
		return commentTagDao.findOne(id);
	}
	
	@SuppressWarnings("unused")
	private List<CommentTag> findAllCommentTags() {
		return commentTagDao.findAll();
	}
	
	
	@SuppressWarnings("unused")
	private boolean updateCommentTag(CommentTag ct) {
		return commentTagDao.updateOne(ct);
	}
	
	@SuppressWarnings("unused")
	private List<Comment> findAllComments() {
		return commentDao.findAll();
	}
}
