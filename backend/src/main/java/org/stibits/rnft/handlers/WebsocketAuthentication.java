package org.stibits.rnft.handlers;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class WebsocketAuthentication implements HandshakeInterceptor {
    @Autowired
    private AuthenticationHandler authenticationHandler;

    private AuthenticatedOnly authenticatedOnlyHandler = new AuthenticatedOnly();
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest servletRequest = this.authenticate(request);
        
        if (servletRequest == null && !this.authenticatedOnly(request, response)) return false;

        Enumeration<String> servletAttributes = servletRequest.getAttributeNames();

        while (servletAttributes.hasMoreElements()) {
            String name = servletAttributes.nextElement();
            attributes.put(name, servletRequest.getAttribute(name));
        }

        return true;
    }

    private boolean authenticatedOnly (ServerHttpRequest request, ServerHttpResponse response) throws Exception {
        if (request instanceof ServletServerHttpRequest && response instanceof ServletServerHttpResponse) {
            ServletServerHttpRequest httpRequest = (ServletServerHttpRequest)request;
            ServletServerHttpResponse httpResponse = (ServletServerHttpResponse)response;

            return this.authenticatedOnlyHandler.preHandle(httpRequest.getServletRequest(), httpResponse.getServletResponse(), null);
        }

        return false;
    }

    private HttpServletRequest authenticate (ServerHttpRequest request) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest httpRequest = (ServletServerHttpRequest)request;
            HttpServletRequest servletRequest = httpRequest.getServletRequest();
            authenticationHandler.preHandle(servletRequest, null, null);

            return servletRequest;
        }

        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
