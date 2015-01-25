package edu.fudan.msg.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.fudan.msg.constant.JSONResult;
import edu.fudan.msg.constant.RequestParameter;
import edu.fudan.msg.pojo.User;
import edu.fudan.msg.util.ExcelUtil;

public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LogManager
			.getLogger(FileUploadServlet.class);

	public int oid = 1;

	public static final String SAVE_FILE_NAME = "upload/student";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String action = request.getParameter("action");
		JSONObject json = null;

		if (null == action || 1 != action.length()) {
			json = JSONResult.INVALID_PARAMETER;
		} else {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			char key = action.charAt(0);
			switch (key) {
			case RequestParameter.RETRIEVE:

				oid = getOid(request);
				System.out.println("oid" + oid);
				json = JSONResult.success();
				break;
			case RequestParameter.UPLOAD:
				json = upload(request, user, oid);
				break;
			default:
				json = JSONResult.INVALID_PARAMETER;
			}
		}

		PrintWriter out = response.getWriter();
		LOG.info(json);
		out.write(json.toString());
		out.flush();
		out.close();
	}

	private JSONObject upload(HttpServletRequest request, User user, int oid) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		JSONObject json = null;
		File file = null;
		String ext = null;
		if (oid <= 0) {
			json = JSONResult.INVALID_PARAMETER;
		} else {
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				try {
					List<?> items = upload.parseRequest(request);
					Iterator<?> it = items.iterator();

					while (it.hasNext()) {
						FileItem item = (FileItem) it.next();
						if (!item.isFormField()) {
							String fileName = item.getName();
							ext = fileName.substring(
									fileName.lastIndexOf(".") + 1,
									fileName.length());

							if (ext.equals("xls") || ext.equals("xlsx")) {
								request.getSession().setAttribute("ext", ext);
								InputStream is = item.getInputStream();
								file = Resources
										.getResourceAsFile(SAVE_FILE_NAME);
								FileOutputStream os = new FileOutputStream(file);

								IOUtils.copy(is, os);
								is.close();
								os.close();
							}
							break;
						}
					}
					json = JSONResult.success();
				} catch (Exception e) {
					LOG.error("Upload Exception: ", e);
				}
			}
		}
		if (null == json) {
			json = JSONResult.UPLOAD_FILE_ERROR;
		} else {
			try {
				ExcelUtil.importStudents(file, ext, oid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	private int getOid(HttpServletRequest request) {
		String oidStr = request.getParameter("oid");
		if (oidStr == null) {
			return 1;
		}
		int oid = Integer.parseInt(oidStr);
		return oid;
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
