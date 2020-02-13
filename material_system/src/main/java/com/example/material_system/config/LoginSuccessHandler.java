package com.example.material_system.config;

import com.example.material_system.entity.College;
import com.example.material_system.mapper.CollegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@SessionAttributes(value = {"notice","college","grade","account"})
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired(required = false)
    CollegeMapper collegeMapper;

    @Autowired
    ConcurrentHashMap<String,String> concurrentHashMap;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        httpServletRequest.getSession().setAttribute("account",username);
        httpServletRequest.getSession().setAttribute("college",collegeMapper.getCollegeAllInformation(Integer.parseInt(username)).getCollege_name());
        if (concurrentHashMap.containsKey(username)){
            httpServletRequest.getSession().setAttribute("notice",concurrentHashMap.get(username));
        }
        concurrentHashMap.remove(username);
        if (userDetails.getAuthorities().toString().contains("ROLE_ADMIN")){
            httpServletResponse.sendRedirect("/admin/adminIndex");
            httpServletRequest.getSession().setAttribute("grade","ROLE_ADMIN");
            return;
        }
        httpServletRequest.getSession().setAttribute("grade","ROLE_USER");
        httpServletResponse.sendRedirect("/");
    }
}
