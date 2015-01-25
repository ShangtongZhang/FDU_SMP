package edu.fudan.msg.util;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.ibatis.io.Resources;

import edu.fudan.msg.util.bean.Email;

/**
 * 邮件发送工具类，邮箱必须支持smtp服务
 * 
 * @author xuri
 * @since 2014-7-16
 */
public class EmailUtil {

	/**
	 * 封装邮件发送时的一些配置信息的一个属性对象
	 */
	// private static Properties props = System.getProperties();
	private static Properties props = new Properties();

	/**
	 * SMTP服务器的地址
	 */
	// private static String hostName = "mail.fudan.edu.cn";
	/**
	 * 设置SMTP的身份认证,true or false
	 */
	// private static String needAuth = "true";
	static {
		try {
			props.load(Resources.getResourceAsStream("config/mail.properties"));
		} catch (IOException e) {
		}
	}

	/**
	 * 发件人的用户名
	 */
	public static String username = props.getProperty("username");
	/**
	 * 发件人的密码
	 */
	private static String password = props.getProperty("password");
	/**
	 * 发送邮件
	 * 
	 * @return
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public static void send(Email email) throws AddressException,
			MessagingException {
		// props.put("mail.smtp.host", hostName);
		// props.put("mail.smtp.auth", needAuth);

		// 专门用来发送邮件的Session会话
		Session session = Session.getInstance(props, null);
		// 整个MIME邮件对象
		MimeMessage mimeMsg = new MimeMessage(session);
		// 用来实现附件添加的组件
		Multipart mp = new MimeMultipart();

		// 添加发送时间
		mimeMsg.setSentDate(email.getSendDate());

		// 添加邮件文本
		BodyPart contentBodyPart = new MimeBodyPart();
		contentBodyPart.setContent(
				"<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"
						+ email.getContent(), "text/html;charset=UTF-8");
		mp.addBodyPart(contentBodyPart);

		// 添加附件
		Set<String> files = email.getFiles();
		for (String file : files) {
			BodyPart fileBodyPart = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(file);
			fileBodyPart.setDataHandler(new DataHandler(fileds));
			fileBodyPart.setFileName(fileds.getName());
			mp.addBodyPart(fileBodyPart);
		}

		mimeMsg.setSubject(email.getTitle()); // 设置邮件主题

		mimeMsg.setFrom(new InternetAddress(email.getFrom()));// 设置发件人地址

		mimeMsg.setRecipients(javax.mail.Message.RecipientType.TO,
				InternetAddress.parse(email.getTo())); // 设置收件人地址

		if (email.getCopyTo() != null) {
			mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC,
					InternetAddress.parse(email.getCopyTo()));// 设置抄送地址
		}
		if (email.getSecretlyCopyTo() != null) {
			mimeMsg.setRecipients(javax.mail.Message.RecipientType.BCC,
					InternetAddress.parse(email.getSecretlyCopyTo()));// 设置暗送地址
		}

		mimeMsg.setContent(mp);
		mimeMsg.saveChanges();

		Transport transport = session.getTransport("smtp");
		// 真正的连接邮件服务器并进行身份验证
		transport.connect((String) props.get("mail.smtp.host"),
				props.getProperty("username"), props.getProperty("password"));
		// 发送邮件
		transport.sendMessage(mimeMsg,
				mimeMsg.getRecipients(javax.mail.Message.RecipientType.TO));
		transport.close();
	}

	/**
	 * @throws MessagingException
	 * @throws AddressException
	 */
	public static void alarmDeveloper(String message) throws AddressException,
			MessagingException {
		Email email = new Email();
		email.setTitle("测试");
		email.setContent(message);
		email.setFrom(props.getProperty("username"));
		email.setTo("10302010050@fudan.edu.cn");
		email.setCopyTo("pyxuri111@163.com");
		send(email);
	}

	public static void main(String[] args) throws AddressException,
			MessagingException {
		String content = "<b>哈哈哈</b><div><b><font color=\"#ff00ff\">嘿嘿嘿</font></b></div><div><b><font color=\"#ff00ff\" size=\"6\" style=\"background-color: rgb(0, 255, 255);\">测试测试</font></b></div>";
		alarmDeveloper(content);
	}
}
