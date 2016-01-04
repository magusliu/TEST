package com.khinf.mobile.web.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.khinf.mobile.web.util.SecurityParameterUtils;

public class SecurityParamRequestWarpper extends HttpServletRequestWrapper {
	private Map<String, String[]> params;

	public SecurityParamRequestWarpper(HttpServletRequest request) {
		super(request);
		Map<String, String[]> ar = request.getParameterMap();
		params = new LinkedHashMap<String, String[]>(ar.size());
		for (String key : ar.keySet()) {
			String[] olds = ar.get(key);
			String[] vals = new String[olds.length];
			for (int i = 0; i < olds.length; i++) {
				vals[i] = SecurityParameterUtils.filter(olds[i]);
			}
			params.put(key, vals);
		}
	}

	@Override
	public String getParameter(String name) {
		String[] ar = this.params.get(name);
		return ar == null ? null : ar[0];
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return params;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(params.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return params.get(name);
	}
}
