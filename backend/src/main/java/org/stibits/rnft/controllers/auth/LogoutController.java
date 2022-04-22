package org.stibits.rnft.controllers.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.Session;
import org.stibits.rnft.model.dao.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${api.version}/auth/logout")
public class LogoutController {
    @Autowired
    private SessionDAO sessionDAO;

    @GetMapping
    public void handleGetRequest (@RequestAttribute(name = "account", required = false) Account account, @RequestAttribute(name = "session", required = false) Session session, HttpServletResponse response) {
        response.setStatus(204);
        
        if (account == null || session == null) return ;

        sessionDAO.deleteSession(session.getSid());

        Cookie cookie = new Cookie("sessionId", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
