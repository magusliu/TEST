package com.khinf.mobile.web.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class PutRequestWrapper extends HttpServletRequestWrapper {

	public final static String CUSTOMER_ATTR_KEY = "customer.attribute.parameter";

	private Map<String, String[]> reqParams;

	@SuppressWarnings("unchecked")
	public PutRequestWrapper(HttpServletRequest request) {
		super(request);
		reqParams = (Map<String, String[]>) request.getAttribute(CUSTOMER_ATTR_KEY);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return reqParams;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(reqParams.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return reqParams.get(name);
	}
}