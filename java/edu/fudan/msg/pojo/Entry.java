package edu.fudan.msg.pojo;

import java.util.ArrayList;

//这个类是所有带id的东西的基类(如果想用这个类去重的话)，用来提供统一的去除重复记录的功能
public class Entry {
	
	@SuppressWarnings("unchecked")
	public static Object deDuplicates(Object raw_entrys_) {
		ArrayList<Entry> raw_entrys = (ArrayList<Entry>)raw_entrys_;
		ArrayList<Entry> new_entries = new ArrayList<Entry>();
		for (Entry e : raw_entrys) {
			if (!isExist(e.getId(), new_entries)) {
				new_entries.add(e);
			}
		}
		return new_entries;
	}
	
	private static boolean isExist(int id, ArrayList<Entry> es) {
		for (Entry e : es) {
			if (id == e.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public int getId() {
		return -1;
	}
}
