package org.merchantech.nftproject.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    String getVersion () {
        return "0.1.0-BETA";
    }
}
