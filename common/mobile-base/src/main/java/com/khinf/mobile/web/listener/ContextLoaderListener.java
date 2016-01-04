package com.khinf.mobile.web.listener;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.khinf.mobile.web.util.WebUtils;

public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		WebUtils.BASE_PATH = event.getServletContext().getContextPath();
		WebUtils.FILE_PATH = event.getServletContext().getRealPath("/");
		WebUtils.APPLICATION_CONTEXT = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		super.contextInitialized(event);
	}

}
