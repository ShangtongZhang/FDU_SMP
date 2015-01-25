package edu.fudan.msg.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.fudan.msg.constant.JSONResult;


public class AJAXExceptionFilter implements Filter{
	private static final Logger LOG = LogManager.getLogger(AJAXExceptionFilter.class);
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
			chain.doFilter(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("Excepion: ", e);
			HttpServletResponse res = (HttpServletResponse)response;
			res.reset();
			res.setContentType("text/json;charset=utf-8");
			PrintWriter out = res.getWriter();
			out.write(JSONResult.SERVER_ERROR.toString());
			out.flush();
			out.close();
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

}
