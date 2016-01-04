package com.khinf.mobile.web.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.daochong.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.khinf.mobile.web.util.JsonAccessException;

public class JsonErrorInterceptor implements HandlerInterceptor {

	private boolean withException;

	public boolean isWithException() {
		return withException;
	}

	public void setWithException(boolean withException) {
		this.withException = withException;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("AFTER:"+this.getClass().getName());
		if (ex != null && (ex instanceof JsonAccessException)) {
			JsonAccessException je = (JsonAccessException) ex;
			try {
				JSONObject error = new JSONObject();
				error.put("code", je.getCode());
				error.put("message", je.getMessage());
				error.put("host",request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
				if (this.isWithException() && je.getCause() != null ) {
					error.put("exceptions", StringUtils.toString(je.getCause()));
				}
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/json;charset=UTF-8");
				try {
					PrintWriter out = response.getWriter();
					out.print(error.toString());
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Throwable ee) {
				ee.printStackTrace();
			}
		}
	}

}
