package com.khinf.mobile.web.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class PutMethodFilter implements Filter {

	private String defaultEncoding = "UTF-8";

	public PutMethodFilter() {

	}

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		if ("PUT".equalsIgnoreCase(req.getMethod())) {
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String org_req_content;
			StringBuilder content = new StringBuilder();
			while ((org_req_content = in.readLine()) != null) {
				content.append(URLDecoder.decode(org_req_content, this.defaultEncoding));
				Map<String, String[]> map = parseRequestParameter(content.toString());
				request.setAttribute(PutRequestWrapper.CUSTOMER_ATTR_KEY, map);
			}
			request = new PutRequestWrapper(req);
			chain.doFilter(request, response);
		}else{
			chain.doFilter(request, response);
		}
	}

	public Map<String, String[]> parseRequestParameter(String content) {
		Map<String, String[]> params = new HashMap<String, String[]>();
		if (content == null || "".equals(content.trim())) {
			return params;
		}
		try {
			String[] tmp = content.split("&");
			for (int i = 0; i < tmp.length; i++) {
				String[] keyValue = tmp[i].split("=");
				params.put(keyValue[0], new String[] { keyValue[1] });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	public void init(FilterConfig fConfig) throws ServletException {
		String code = fConfig.getInitParameter("defaultEncoding");
		if (code != null) {
			this.defaultEncoding = code;
		}
	}
}