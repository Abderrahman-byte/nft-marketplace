package org.stibits.rnft.controllers.marketplace;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stibits.rnft.entities.Account;
import org.stibits.rnft.entities.Token;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.DataIntegrityError;
import org.stibits.rnft.errors.NotFoundError;
import org.stibits.rnft.repositories.NFTokenDAO;
import org.stibits.rnft.repositories.TransactionDAO;

@RestController
@RequestMapping("/api/${api.version}/marketplace/buy")
public class TransactionController {
	@Autowired
	TransactionDAO transactionDAO;
	@Autowired
	NFTokenDAO tokenDao;
	
	 @PostMapping
	    public Map<String, Object> handlePostRequest (@RequestBody Map<String, Object> data, @RequestAttribute("account") Account account) throws ApiError {
	        Map<String, Object> response = new HashMap<>();

	        if (!data.containsKey("id") || !data.get("id").getClass().equals(String.class)) throw new NotFoundError();

	        try {
	        	Token t = tokenDao.selectToken((String)data.get("id"));
	        	response.put("token", t);
	        	
	        	Account accFrom = t.getOwner();
	        	//generate QR code
	          transactionDAO.insertTransaction((String)data.get("id"), accFrom.getId(), account.getId(), t.getSettings().getPrice());
	          t.setOwner(account);
	          response.put("success", true);
	           
	        } catch (DataIntegrityViolationException ex) {
	            throw new DataIntegrityError("Token with this id does not exist", "id");
	           
	        }
	        
	        return response;
	    }


}
