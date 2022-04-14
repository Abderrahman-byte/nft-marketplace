package org.merchantech.nftproject.controllers.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/login")
public class login {
    @GetMapping
    Map<Object, Object> handleGetRequest () {
        Map<Object, Object> data = new HashMap<>();
        data.put("errors", List.of("login page is not implemented"));
        data.put("ok", false);
        
        return data;
    }
}
