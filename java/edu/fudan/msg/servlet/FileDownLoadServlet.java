package edu.fudan.msg.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.fudan.msg.pojo.User;

public class FileDownLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager
			.getLogger(FileDownLoadServlet.class);

	private static final String FILE_NAME = "student.xlsx";
	private static final String FILE_PATH = "template/";

	public FileDownLoadServlet() {
		super();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (null == user) {
			response.sendRedirect("index.jsp");
			return;
		}

		try {
			response.reset();
			response.setContentType("multipart/form-data");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(FILE_NAME.getBytes("utf-8"), "ISO-8859-1"));
			FileInputStream fis = new FileInputStream(
					Resources.getResourceAsFile(FILE_PATH + FILE_NAME));
			OutputStream os = response.getOutputStream();
			IOUtils.copy(fis, os);
			return;
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Template download exception: ", e);
			response.sendRedirect("error.html");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
