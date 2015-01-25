package edu.fudan.msg.dao;

import java.util.List;

import edu.fudan.msg.pojo.Tag;

public interface TagDao extends GenericDao<Tag>{
	
	List<Tag> getTagsByComment(int cid);
}
