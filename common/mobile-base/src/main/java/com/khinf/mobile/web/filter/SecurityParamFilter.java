package com.khinf.mobile.web.filter;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.khinf.mobile.web.util.SecurityParameterUtils;

public class SecurityParamFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {

		if (filterConfig.getInitParameter("sqlKeywords") != null) {
			String keywords = filterConfig.getInitParameter("sqlKeywords");
			SecurityParameterUtils.REG_LIST.put("sqlKeywords",
					Pattern.compile("(.*[^a-zA-Z_0-9]{1}(" + keywords + ")|(" + keywords + ")){1}[^a-zA-Z_0-9]{1}.*"));

		}
		if (filterConfig.getInitParameter("functiosn") != null) {
			String functions = filterConfig.getInitParameter("functiosn");
			SecurityParameterUtils.REG_LIST.put("sqlFunctions",
					Pattern.compile("(.*[^a-zA-Z_0-9]{1}(" + functions + ")|(" + functions + ")){1}\\s*\\(.*\\).*"));

		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Map<String, String[]> ar = request.getParameterMap();
		for (String key : ar.keySet()) {
			for (String val : ar.get(key)) {
				SecurityParameterUtils.check(val, request.getCharacterEncoding());
			}
		}
		chain.doFilter(new SecurityParamRequestWarpper((HttpServletRequest) request), response);
	}

	public void destroy() {

	}

	public static void main(String[] args) {
		String str = "";
		Pattern p = Pattern.compile(str, Pattern.CASE_INSENSITIVE);
		String s = "a \\u0023 a *  dual";
		Matcher m = p.matcher(s);
		System.out.println(s.indexOf("#"));
		System.out.println(m.matches());
		System.out.println(m.groupCount());
	}
}
