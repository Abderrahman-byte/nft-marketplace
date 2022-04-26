package org.stibits.rnft.handlers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.stibits.rnft.entities.Account;
import org.stibits.rnft.errors.ApiError;
import org.stibits.rnft.errors.AuthenticationRequiredError;
import org.stibits.rnft.errors.UnverifiedEmailError;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticatedOnly implements HandlerInterceptor {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) return true;

        Account account = (Account)request.getAttribute("account");
        Map<String, Object> message = new HashMap<>();
        ApiError error = null;

        if (account != null && account.isVerified()) return true;

        error = account == null ? new AuthenticationRequiredError() : new UnverifiedEmailError();

        message.put("success", false);
        message.put("error", error);

        response.setStatus(403);
        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().write(objectMapper.writeValueAsString(message).getBytes());

        return false;
    }
}
