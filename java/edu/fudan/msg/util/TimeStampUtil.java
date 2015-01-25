package edu.fudan.msg.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeStampUtil {

	public static String TimestampToString() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		Timestamp time = new Timestamp(System.currentTimeMillis());// 获取系统当前时间
		String createTime = df.format(time);
		return createTime;
	}
}
