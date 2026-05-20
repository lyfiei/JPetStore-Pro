package com.csu.jpetstore.config;

import com.csu.jpetstore.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Set<String> WHITELIST = Set.of(
            "/api/v1/auth/login",
            "/api/v1/auth/login/email",
            "/api/v1/auth/register",
            "/api/v1/auth/send-code",
            "/api/v1/auth/validate",
            "/api/v1/auth/oauth/github/authorize",
            "/api/v1/auth/oauth/github/callback"
    );

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (WHITELIST.contains(path)) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token无效\",\"data\":null}");
            return false;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\",\"data\":null}");
            return false;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        request.setAttribute("username", username);
        return true;
    }
}
