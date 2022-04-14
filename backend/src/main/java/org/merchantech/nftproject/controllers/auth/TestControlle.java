package org.merchantech.nftproject.controllers.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class TestControlle {
  
	@GetMapping(path = "/test")
	public String test(Model model) {
		
		return "index";	
	}

}
