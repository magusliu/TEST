package com.khinf.mobile.web.util;

import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.springframework.context.ApplicationContext;

import com.alibaba.druid.filter.config.ConfigTools;

public class WebUtils {

	public static ApplicationContext APPLICATION_CONTEXT;
	public static String BASE_PATH;
	public static String FILE_PATH;

	public static final String SP = "@@CRLF@@";

	static ApplicationContext applicationContext = null;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static X509Certificate getX509Certificate(HttpServletRequest request) {
		if (!request.getScheme().equalsIgnoreCase("https"))
			return null;
		Object obj = request.getAttribute("javax.servlet.request.X509Certificate");
		if (obj instanceof X509Certificate[]) {
			X509Certificate[] ar = ((X509Certificate[]) obj);
			if (ar.length > 0) {
				return ar[0];
			}
		}
		return null;
	}

	public static String getBase(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath();
	}

	public static String getHost(HttpServletRequest request) {
		return request.getServerName();
	}

	public static String getRootFilePath() {
		return FILE_PATH;
	}

	public static String getRoot() {
		return BASE_PATH;
	}

	public static Map<String, String> getRequestMap(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String str = en.nextElement();
			map.put(str, request.getParameter(str));
		}
		return map;
	}

	public static Map<String, String> getRequestMapWithout(HttpServletRequest request, String... ar) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String str = en.nextElement();
			boolean find = false;
			for (String s : ar) {
				if (str.equals(s)) {
					find = true;
					break;
				}
			}
			if (find) {
				continue;
			}
			map.put(str, request.getParameter(str));
		}
		return map;
	}

	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;

	}

	public static String getCookies(String name, HttpServletRequest request) {
		return getCookiesMap(request).get(name);
	}

	public static void setCookies(String name, String value, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		response.addCookie(cookie);
	}

	public static void setCookies(String name, String value, int seconds, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(seconds);
		response.addCookie(cookie);
	}

	public static Map<String, String> getCookiesMap(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : request.getCookies()) {
				map.put(cookie.getName(), cookie.getValue());
			}
		}
		return map;
	}

	public static Map<String, String> getHeaderMap(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Enumeration<String> en = request.getHeaderNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			map.put(key, request.getHeader(key));
		}
		return map;
	}

	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String key = en.nextElement();
			map.put(key, request.getParameter(key));
		}
		return map;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(ConfigTools.encrypt(""));
	}

	public static Map<String, String> toMap(String str) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (str == null || str.trim().length() == 0)
			return map;
		for (String s : str.trim().split("\\r\\n")) {
			s = s.trim();
			if (s.startsWith("#"))
				continue;
			int pos = s.indexOf("=");
			if (pos == -1)
				continue;
			String key = s.substring(0, pos).trim();
			String value = s.substring(pos + 1).trim().replaceAll(SP, "\r\n");
			map.put(key, value);
		}
		return map;
	}

	public static String toString(Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		if (map != null && map.size() > 0) {
			for (String key : map.keySet()) {
				sb.append(key + "=" + (map.get(key) == null ? "" : map.get(key).replaceAll("\\r\\n", SP)) + "\r\n");
			}
		}
		return sb.toString();
	}

	public static HttpServletRequest getRequest(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof HttpServletRequest) {
				request = (HttpServletRequest) args[i];
				break;
			}
		}
		return request;
	}

	public static HttpServletResponse getResponse(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		HttpServletResponse response = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof HttpServletRequest) {
				response = (HttpServletResponse) args[i];
				break;
			}
		}
		return response;
	}
}
