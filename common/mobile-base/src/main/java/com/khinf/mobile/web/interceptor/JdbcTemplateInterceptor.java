package com.khinf.mobile.web.interceptor;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.daochong.lang.JdbcTemplate;
import org.daochong.lang.JdbcTemplateFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.khinf.mobile.web.util.JsonAccessException;

public class JdbcTemplateInterceptor implements HandlerInterceptor {

	private List<String> configList;
	private boolean showSql;
	private boolean ignoreNullValue;
	private boolean ignoreMappingError;
	private JdbcTemplateFactory factory;

	public JdbcTemplateFactory getFactory() {
		return factory;
	}

	public void setFactory(JdbcTemplateFactory factory) {
		this.factory = factory;
	}

	public boolean isIgnoreMappingError() {
		return ignoreMappingError;
	}

	public List<String> getConfigList() {
		return configList;
	}

	public void setConfigList(List<String> configList) {
		this.configList = configList;
	}

	public void setIgnoreMappingError(boolean ignoreMappingError) {
		this.ignoreMappingError = ignoreMappingError;
	}

	public boolean isIgnoreNullValue() {
		return ignoreNullValue;
	}

	public void setIgnoreNullValue(boolean ignoreNullValue) {
		this.ignoreNullValue = ignoreNullValue;
	}

	public boolean isShowSql() {
		return showSql;
	}

	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		JdbcTemplate jdbc = factory.getJdbcTemplate();
		System.out.println("AFTER:" + jdbc + ":" + Thread.currentThread().getId());
		jdbc.setShowSql(this.isShowSql());
		jdbc.setIgnoreNullValue(this.isIgnoreNullValue());
		jdbc.setIgnoreMappingError(this.isIgnoreMappingError());
		jdbc.beginTran();
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("AFTER:" + this.getClass().getName());
		boolean commit = true;
		Throwable ce = ex;
		while (ce != null) {
			if (ce instanceof SQLException) {
				commit = false;
				break;
			} else if (ce instanceof JsonAccessException) {
				commit = false;
				break;
			} else if (ce instanceof RuntimeException) {
				commit = false;
				break;
			}
			if (ce.equals(ce.getCause())) {
				break;
			}
			ce = ce.getCause();
		}
		JdbcTemplate jdbc = factory.getJdbcTemplate();
		System.out.println("AFTER:" + jdbc + ":" + Thread.currentThread().getId());
		if (jdbc.isTran()) {
			if (commit) {
				jdbc.commit();
			} else {
				jdbc.rollback();
			}
		}
	}

}
