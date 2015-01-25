package edu.fudan.msg.util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	public static void importStudents(File file, String ext, Integer oid)
			throws Exception {
		if (null == file || null == ext || -1 == oid) {
			return;
		} // invalid parameter
		Workbook wb = null;
		FileInputStream is = new FileInputStream(file);
		if (ext.equals("xls")) {
			wb = new HSSFWorkbook(is);
		} else {
			wb = new XSSFWorkbook(is);
		}

		int cntSheets = wb.getNumberOfSheets();
		Connection conn = getConnection();
		PreparedStatement pre_stu = null;
		PreparedStatement pre_org_stu = null;
		ResultSet rs = null;
		String sql_student = "insert into tbl_student (studentNo,name,phone,email,remark,createTime)values(?,?,?,?,?,?)";
		String sql_org_stu = "insert into tbl_org_student (sid,oid,createdTime)values(?,?,?)";
		String createTime = TimeStampUtil.TimestampToString();
		for (int i = 0; i < cntSheets; i++) {
			Sheet sheet = wb.getSheetAt(i);
			int cnt = 0;
			for (Row row : sheet) {
				if (0 == cnt) {
					cnt++;
					continue;
				} // 表头
					// student number
				Cell cell = row.getCell(0);
				String studentNo = cell.toString();
				// student name
				cell = row.getCell(1);
				String name = cell.toString();
				// student phone
				cell = row.getCell(2);
				String phone = cell.toString();
				// student email
				cell = row.getCell(3);
				String email = cell.toString();
				cell = row.getCell(4);
				String remark = null;
				if (cell != null) {
					remark = cell.toString();
				}
				pre_stu = conn.prepareStatement(sql_student,
						Statement.RETURN_GENERATED_KEYS);
				pre_stu.setString(1, studentNo);
				pre_stu.setString(2, name);
				pre_stu.setString(3, phone);
				pre_stu.setString(4, email);
				pre_stu.setString(5, remark);
				pre_stu.setString(6, createTime);

				pre_stu.addBatch();

				if (cnt % 200 == 0) {
					try {
						pre_stu.executeBatch();
					} catch (Exception e) {
						System.out.println(e);
					}
					rs = pre_stu.getGeneratedKeys();
					while (rs.next()) {
						int id = rs.getInt(1);
						pre_org_stu = conn.prepareStatement(sql_org_stu,
								Statement.RETURN_GENERATED_KEYS);
						pre_org_stu.setInt(1, id);
						pre_org_stu.setInt(2, oid);
						pre_org_stu.setString(3, createTime);
						pre_org_stu.addBatch();
					}
					pre_org_stu.executeBatch();
				}
				cnt++;
				try {
					pre_stu.executeBatch();
				} catch (Exception e) {
					System.out.println(e);
				}

				rs = pre_stu.getGeneratedKeys();
				while (rs.next()) {
					int id = rs.getInt(1);
					pre_org_stu = conn.prepareStatement(sql_org_stu,
							Statement.RETURN_GENERATED_KEYS);
					pre_org_stu.setInt(1, id);
					pre_org_stu.setInt(2, oid);
					pre_org_stu.setString(3, createTime);
					pre_org_stu.addBatch();
				}
				pre_org_stu.executeBatch();
			}
			if (cntSheets % 50 == 0) {
				System.gc();
			}
		}
		if (pre_stu != null) {
			pre_stu.close();
		}
		if (pre_org_stu != null) {
			pre_org_stu.close();
		}
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}
		is.close();
		System.gc();
	}

	public static Connection getConnection() {
		Properties props = new Properties();
		Connection conn = null;
		try {
			props.load(Resources
					.getResourceAsStream("config/dbConfig.properties"));
			// 驱动程序名
			String driver = (String) props.get("driver");
			// URL指向要访问的数据库名scutcs
			String url = (String) props.get("url");

			// MySQL配置时的用户名
			String user = (String) props.get("username");

			// MySQL配置时的密码
			String password = (String) props.get("password");
			// 加载驱动程序
			Class.forName(driver);
			// 连续数据库
			conn = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
