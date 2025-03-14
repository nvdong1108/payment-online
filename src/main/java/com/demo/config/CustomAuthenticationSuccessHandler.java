package com.demo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Component;

import com.demo.entity.LoginLog;
import com.demo.repository.LoginLogRepository;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;


@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler   implements AuthenticationSuccessHandler{
    
     @Autowired
    private LoginLogRepository loginLogRepository;
    
    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
                String targetUrl = determineTargetUrl(authentication);
                getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            log.info("=====================>   role: " + role);
            if (role.equals("ROLE_ADMIN")) {
                return "/dashboard"; 
            } else if (role.equals("ROLE_USER")) {
                return "/checkout";
            }
        }
        return "/dashboard"; 
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        String ipAddress = request.getRemoteAddr();
        Date loginTime = new Date();
        String userAgent = request.getHeader("User-Agent");
        LoginLog loginLog = new LoginLog(username, ipAddress,loginTime, userAgent);
        loginLogRepository.save(loginLog);
        
        handle(request, response, authentication);

    }
}