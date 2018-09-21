package org.paingan.zuul.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GatewayController {

	@RequestMapping(value = "/")
	public String welcome() throws Exception {
		return "index";
	}
	
	@RequestMapping(value = "/login")
	public String login() throws Exception {
		return "login";
	}
}
