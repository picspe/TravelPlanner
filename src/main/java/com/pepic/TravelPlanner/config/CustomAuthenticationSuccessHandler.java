package com.pepic.TravelPlanner.config;

import com.pepic.TravelPlanner.models.User;
import com.pepic.TravelPlanner.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        final HttpSession session = httpServletRequest.getSession(false);
        if(session != null) {
            String username;
            if(authentication.getPrincipal() instanceof User) {
                username = ((User) authentication.getPrincipal()).getUsername();
            } else {
                username = authentication.getName();
            }
            User user = userRepository.findByUsername(username);
            session.setAttribute("user", user);
        }
        clearAuthenticationAttributes(httpServletRequest);
    }

    private void clearAuthenticationAttributes(final HttpServletRequest httpServletRequest) {
        final HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
