package org.stibits.rnft.controllers.auth;

import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.stibits.rnft.repositories.AccountDAO;

@Controller
@RequestMapping("/verify-email")
public class VerifyEmailController {
    @Autowired
    private AccountDAO accountDAO;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @GetMapping
    String handleGetRequest (@ModelAttribute("payload") Map<String, Claim> payload) {
        if (payload == null || payload.get("accountId") == null || payload.get("action") == null) return "verify-email-failed";
        
        String accountId = payload.get("accountId").asString();
        String action = payload.get("action").asString();

        if (accountId.length() <= 0 || !action.equals("verifyEmail")) return "verify-email-failed";

        boolean verified = accountDAO.setAccountAsVerified(accountId);

        if (!verified) return "verify-email-failed";

        return "verify-email";
    }

    @ModelAttribute
    public void parseJWTPayload (@RequestParam(name = "tq", required = false) String token, Model model) {
        if (token == null || token.length() <= 0) {
            model.addAttribute("payload", null);
            return ;
        }

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier  verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decoded = verifier.verify(token);
            Map<String, Claim> payload = decoded.getClaims();
            model.addAttribute("payload", payload);
        } catch (JWTVerificationException ex) {
            model.addAttribute("payload", null);
        }        
    }

}