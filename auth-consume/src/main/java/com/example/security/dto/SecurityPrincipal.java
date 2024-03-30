package com.example.security.dto;

import com.example.entity.AuthUser;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SecurityPrincipal {

    private static  SecurityPrincipal securityPrincipal=null;

    private Authentication principal= SecurityContextHolder.getContext().getAuthentication();

    private static UserService userService;

    @Autowired
    private SecurityPrincipal(UserService userService){
        this.userService=userService;
    }

    public static  SecurityPrincipal getInstance(){
        securityPrincipal=new SecurityPrincipal(userService);
        return  securityPrincipal;
    }

    public AuthUser getLoggedInPrincipal(){
        if(principal!=null){
            UserDetails loggedInPrincipal=(UserDetails) principal.getPrincipal();
            return  userService.findByUserName(loggedInPrincipal.getUsername());
        }

        return  null;
    }

    public Collection<?>getLoggedPrincipalAuthorities(){
        return ((UserDetails)principal.getPrincipal()).getAuthorities();
    }

}
