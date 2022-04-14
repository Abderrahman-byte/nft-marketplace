package org.merchantech.nftproject.controllers.auth;

import org.merchantech.nftproject.model.bo.Account;
import org.merchantech.nftproject.model.dao.AccountDAO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/auth/login")
public class LoginController {
	AccountDAO daoacc;
    Account  account;
    
    public LoginController() {
		
	}
    
    @RequestMapping(" ")
	public String viewHomePage(Model model) {

	
	return "index";
	}
    
    
   public String verify(@ModelAttribute("username") String username, @ModelAttribute("password") String password) {
	
	   account = daoacc.getAccountByUsername(username);
	   
	   if(account == null) {
		   return "redirect:/register";
	   }
	   else if (account.getPassword().equals(password))
	   {
		   return "";
	   }
	   else {
		   return "register";
	   }
   
   }

}
