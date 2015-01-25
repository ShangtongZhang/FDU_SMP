package edu.fudan.msg.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import edu.fudan.msg.pojo.Organization;

//OrgNode是维护的org树中的一个节点，subNodes是其所有子节点，是对org的进一步封装
//每个user都有一颗自己的org树，此外还有一个所有org组成的树

public class OrgNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//节点属性
	
	//user没有这个节点的权限，这个节点也不需要做为路径显示
	public static final int NONDISPLAYED = 0;
	
	//user有这个节点的权限
	public static final int PRIVILEGED = 1;
	
	//表示该节点只是作为路径显示，user并没有这个节点的权限
	public static final int DISPLAYED = 2;

	//深度clone一个对象
	public static Object deepClone(Object src) {
		Object o = null;
		try {
			if (src != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(src);
				oos.close();
				ByteArrayInputStream bais = new ByteArrayInputStream(
						baos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				o = ois.readObject();
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	}

	//父节点
	private OrgNode parentOrgNode = null;

	private Organization org;

	private int color = OrgNode.NONDISPLAYED;

	public OrgNode(Organization org) {
		this.org = org;
	}

	public int getPid() {
		return org.getPid();
	}

	public int getId() {
		return org.getId();
	}

	public void addChild(OrgNode c) {
		subNodes.add(c);
		c.parentOrgNode = this;
	}

	public Organization getOrganization() {
		return org;
	}

	@Override
	public String toString() {
		return org.toString();
	}

	public ArrayList<OrgNode> getSubNodes() {
		return subNodes;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public OrgNode getParent() {
		return parentOrgNode;
	}

	//所有子节点
	private ArrayList<OrgNode> subNodes = new ArrayList<OrgNode>();

	public void removeChild(OrgNode n) {
		subNodes.remove(n);
	}

}