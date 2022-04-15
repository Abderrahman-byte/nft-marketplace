package org.merchantech.nftproject.controllers.auth;

import java.util.HashMap;
import java.util.Map;

import org.merchantech.nftproject.model.bo.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/auth/isLoggedIn")
public class isLoggedInController {
    @GetMapping
    Map<String, Object> handleGetRequest (@RequestAttribute(name = "account", required = false) Account account) {
        Map<String, Object> response = new HashMap<>();
        response.put("isLoggedIn", account != null);

        return response;
    }
}
