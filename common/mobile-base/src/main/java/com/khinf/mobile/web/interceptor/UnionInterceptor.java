package com.khinf.mobile.web.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UnionInterceptor implements HandlerInterceptor {

	private List<HandlerInterceptor> interceptors;

	public List<HandlerInterceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<HandlerInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (this.getInterceptors() != null) {
			for (HandlerInterceptor hi : this.getInterceptors()) {
				if (!hi.preHandle(request, response, handler)) {
					return false;
				}
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (this.getInterceptors() != null) {
			for (HandlerInterceptor hi : this.getInterceptors()) {
				hi.postHandle(request, response, handler, modelAndView);
			}
		}
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (this.getInterceptors() != null) {
			for (HandlerInterceptor hi : this.getInterceptors()) {
				hi.afterCompletion(request, response, handler, ex);
			}
		}
	}

}
