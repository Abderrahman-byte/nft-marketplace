package org.stibits.rnft.handlers;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.stibits.rnft.model.bo.Account;
import org.stibits.rnft.model.bo.Session;
import org.stibits.rnft.model.dao.AccountDAO;
import org.stibits.rnft.model.dao.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationHandler implements HandlerInterceptor {
    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookies[] = request.getCookies();

        if (cookies == null) return true;

        for (Cookie cookie : cookies) {
            if (!cookie.getName().equals("sessionId")) continue;

            String sessionId = cookie.getValue();

            if (sessionId == null || sessionId.equals("")) break;

            Session session = sessionDAO.getSessionById(sessionId);
            
            if (session == null || session.getPayload() == null || !session.getPayload().containsKey("currentUser")) break;
            
            Map<String, Object> payload = session.getPayload();
            String accountId = (String)payload.get("currentUser");
            Account account = accountDAO.getAccountById(accountId);
            
            if (account == null) break;
            
            request.setAttribute("account", account);
            request.setAttribute("session", session);
            request.setAttribute("accountId", accountId);

            break;
        }

        return true;
    }
}
