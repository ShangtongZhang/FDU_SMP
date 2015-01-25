package edu.fudan.msg.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.fudan.msg.service.StudentService;

public class DataImportUtil {
	private static final Logger LOG = LogManager.getLogger(DataImportUtil.class);
	private static StudentService service;
	static {
		service = new StudentService();
	}
	
	public static void importStudents(File file, String ext, ArrayList<Integer> cids) throws Exception{
		Workbook wb = null;
		InputStream is = new FileInputStream(file);
		if (ext.equals("xls")) {
			wb = new HSSFWorkbook(is);
		}else{
			wb = new XSSFWorkbook(is);
		}
		
		Sheet sheet = wb.getSheetAt(0);
		ArrayList<Integer> sids = new ArrayList<Integer>();
		for(Row row : sheet){
			// student number #523112 #341f0c
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
			
			//Student st = service.addStudent(studentNo, name, phone, email);
			//if(null != st){ sids.add(st.getId()); }
		}
		// add relation to every class and its father class
		//service.addStudentsToClasses(sids, cids);
	}
	
	public static void main(String args[]) throws Exception{
	}
}
