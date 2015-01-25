package edu.fudan.msg.constant;

import java.io.IOException;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JSONResult {
	private static final Logger LOG = LogManager.getLogger(JSONResult.class);

	public static final String OK = "OK";
	public static final String ERROR = "ERROR";
	public static final String RESULT = "Result";
	public static final String MESSAGE = "Message";
	public static final String RECORD = "Record";
	public static final String RECORDS = "Records";
	public static final String STUDENTS = "Students";
	public static final String STUDENT = "Student";
	public static final String TOTAL_PAGE_NUM = "TotalPageSize";
	public static final String SUM = "Sum";
	public static final String SESSION = "Session";
	public static final String DATA = "Data";

	// Ajax response json
	public static final JSONObject SESSION_TIMEOUT = new JSONObject();
	public static final JSONObject INVALID_PARAMETER = new JSONObject();
	public static final JSONObject INVALID_ACCESS = new JSONObject();
	public static final JSONObject DB_EEROR = new JSONObject();
	public static final JSONObject SERVER_ERROR = new JSONObject();
	public static final JSONObject REGISTER_EXIST = new JSONObject();
	public static final JSONObject LOGIN_ERROR = new JSONObject();
	public static final JSONObject UPLOAD_FILE_ERROR = new JSONObject();
	public static final JSONObject UNAUTHENTICATED_ERROR = new JSONObject();
	public static final JSONObject NO_DATA = new JSONObject();
	public static final JSONObject ASSIGN_AUTHORITY_ERROR = new JSONObject();

	static {
		try {
			Properties props = new Properties();
			props.load(Resources
					.getResourceAsStream("config/message.properties"));

			// session time out
			SESSION_TIMEOUT.accumulate(RESULT, ERROR);
			SESSION_TIMEOUT.accumulate(SESSION, true);
			SESSION_TIMEOUT.accumulate(MESSAGE,
					props.getProperty("SESSION_TIMEOUT"));
			// invalid parameter
			INVALID_PARAMETER.accumulate(RESULT, ERROR);
			INVALID_PARAMETER.accumulate(MESSAGE,
					props.getProperty("INVALID_PARAMETER"));
			// invalid access
			INVALID_ACCESS.accumulate(RESULT, ERROR);
			INVALID_ACCESS.accumulate(MESSAGE,
					props.getProperty("INVALID_ACCESS"));
			// database operation error
			DB_EEROR.accumulate(RESULT, ERROR);
			DB_EEROR.accumulate(MESSAGE, props.getProperty("DB_EEROR"));
			// server exception
			SERVER_ERROR.accumulate(RESULT, ERROR);
			SERVER_ERROR.accumulate(MESSAGE, props.getProperty("SERVER_ERROR"));
			// login error
			LOGIN_ERROR.accumulate(RESULT, ERROR);
			LOGIN_ERROR.accumulate(MESSAGE, props.getProperty("LOGIN_ERROR"));
			// register error: already exist
			REGISTER_EXIST.accumulate(RESULT, ERROR);
			REGISTER_EXIST.accumulate(MESSAGE,
					props.getProperty("REGISTER_EXIST"));
			// unauthenticated error
			UNAUTHENTICATED_ERROR.accumulate(RESULT, ERROR);
			UNAUTHENTICATED_ERROR.accumulate(MESSAGE,
					props.getProperty("UNAUTHENTICATED_ERROR"));
			// no data
			NO_DATA.accumulate(RESULT, ERROR);
			NO_DATA.accumulate(MESSAGE, props.getProperty("NO_DATA"));
			// upload file error
			UPLOAD_FILE_ERROR.accumulate(RESULT, ERROR);
			UPLOAD_FILE_ERROR.accumulate(MESSAGE,
					props.getProperty("UPLOAD_FILE_ERROR"));
			
			ASSIGN_AUTHORITY_ERROR.accumulate(RESULT, ERROR);
			ASSIGN_AUTHORITY_ERROR.accumulate(MESSAGE, props.getProperty("ASSIGN_AUTHORITY_ERROR"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error("Init JSONResult Exception: ", e);
		}
	}

	private JSONResult() {
	}

	// request success
	public static JSONObject success() {
		JSONObject json = new JSONObject();

		json.accumulate(RESULT, OK);
		return json;
	}
}
