package org.merchantech.nftproject.controllers.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/login")
public class login {
    @GetMapping
    String handleGetRequest () {
        return "This path is not implemented";
    }
}
