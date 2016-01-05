package com.khinf.mobile.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.khinf.mobile.web.filter.WarningException;

public class SecurityParameterUtils {
	public static Map<String, Pattern> REG_LIST = new HashMap<String, Pattern>();
	public static String sqlKeywords = "insert|update|delete|select|from|where|group|order|and|or|set|exec|execute";
	public static String sqlFunctions = "exists|count|substr|max|min|avg|left|mid";

	static {
		REG_LIST.put("包含chr()", Pattern.compile("(.*[^a-zA-Z_0-9]{1}chr|chr)\\s*\\(\\s*\\d.\\s*\\).*"));
		REG_LIST.put("sqlKeywords", Pattern
				.compile("(.*[^a-zA-Z_0-9]{1}(" + sqlKeywords + ")|(" + sqlKeywords + ")){1}[^a-zA-Z_0-9]{1}.*"));
		REG_LIST.put("sqlFunctions",
				Pattern.compile("(.*[^a-zA-Z_0-9]{1}(" + sqlFunctions + ")|(" + sqlFunctions + ")){1}\\s*\\(.*\\).*"));
	}

	public static void check(String val, String charSet) throws WarningException {
		try {
			val = URLDecoder.decode(val, charSet);
		} catch (UnsupportedEncodingException e) {
			throw new WarningException("charset " + charSet + " not support");
		}
		for (int i = 0; i < val.length(); i++) {
			int c = val.charAt(i);
			if (c == 127 || (c < 32 && c != 10 && c != 13)) {
				throw new WarningException("Spec Chars");
			}
		}
		if (val.indexOf("<?xml ") >= 0) {
			throw new WarningException("Not Support XML");
		}
		for (String key : REG_LIST.keySet()) {
			Pattern p = REG_LIST.get(key);
			Matcher m = p.matcher(val);
			if (m.matches()) {
				String title = m.group(m.groupCount() - 1);
				throw new WarningException(key + ":" + title);
			}
		}
	}

	public static String filter(String val) {
		if (val == null)
			return val;
		return val.replaceAll("'", "''").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\/|\\/|\\||:|\\?|\\*|\"|<|>|\\p{Cntrl}", "_");
	}
}
