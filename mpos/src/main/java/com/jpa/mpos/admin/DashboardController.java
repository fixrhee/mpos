package com.jpa.mpos.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hazelcast.core.HazelcastInstance;

@Controller
@RequestMapping("/")
public class DashboardController {

	@Autowired
	private AdminProcessor adminProcessor;
	@Autowired
	private HazelcastInstance instance;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String login(Model model) {
		// model.addAttribute("key", contextLoader.getSiteKey());
		// model.addAttribute("login", new Login());
		return "index";
	}
}
