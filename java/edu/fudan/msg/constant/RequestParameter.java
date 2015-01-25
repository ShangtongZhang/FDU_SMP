package edu.fudan.msg.constant;

public class RequestParameter {
	// CRUD operation indicator
	public static final char RETRIEVE = 'r';
	public static final char CREATE = 'c';
	public static final char UPDATE = 'u';
	public static final char DELETE = 'd';
	public static final char SEARCH = 's';

	// organization servlet
	public static final char CHANGE_ORGANIZATION = 'o';
	// student servlet
	public static final char UPLOAD = 'p';
	public static final char ADD_TO_ORGANIZATION = 'a';// 将学生添加到其他组织

	// user servlet
	public static final char LOGIN = 'i';
	public static final char LOGOUT = 'o';
	public static final char REGISTER = 'g';//
	public static final char RETRIEVE_USERS = 'm';// 管理员获取全部信息
	public static final char MANAGE = 'm';// 管理员赋予权限
	public static final char SELF_INFO = 's'; // 获取用户自己的信息
	public static final char GET_AUTHORITYTree = 't';//为用户分配权限
	public static final char ASSIGN_AUTHORITY = 'a';//为用户分配权限
	public static final char GET_AUTHORITY = 'e';//为用户分配权限
	
	// evaulate servlet
	public static final char TAG = 't';
	public static final char ADD_TAG = 'a';
	public static final char SEARCH_COMMENT = 'f';
	public static final char GET_STATISTIC = 's';


	public static int parseInt(String str) {
		int rs = -1;
		try {
			rs = Integer.parseInt(str);
		} catch (Exception e) {
		}
		return rs;
	}

	public static int[] parseInts(String[] strs) {
		if (null == strs) {
			return new int[0];
		}
		int len = strs.length;
		int[] rts = new int[len];
		for (int i = 0; i < len; i++) {
			try {
				rts[i] = Integer.parseInt(strs[i]);
			} catch (Exception e) {
				continue;
			}
		}
		return rts;
	}
}
