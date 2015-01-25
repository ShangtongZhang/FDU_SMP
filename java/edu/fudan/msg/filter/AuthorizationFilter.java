package edu.fudan.msg.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.fudan.msg.constant.JSONResult;
import edu.fudan.msg.pojo.User;

/**
 * Ajax request authorization.
 * */
public class AuthorizationFilter implements Filter {
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (null != user) {
			chain.doFilter(request, response);
		} else {
			response.setContentType("text/json;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.write(JSONResult.SESSION_TIMEOUT.toString());
			out.flush();
			out.close();
		}
	}

	@Override
	public void destroy() {
	}
}
