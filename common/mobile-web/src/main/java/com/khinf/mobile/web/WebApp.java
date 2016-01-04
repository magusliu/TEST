package com.khinf.mobile.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebApp {
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public Object test(User user) {
		// user.setId("ABC");
		// user.setName("O");
		// throw new JsonAccessException(1105);
		return user;
	}
}
