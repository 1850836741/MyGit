package com.example.oauth2_server.config.entity;
import com.example.oauth2_server.entity.User;
import com.example.oauth2_server.r_cache.user_cache.UserCacheTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity用来鉴别对象是否为合法用户的对象
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserCacheTool userCacheTool;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (userCacheTool.getUserByIdFromCache(Integer.valueOf(s))!=null){
            User user = userCacheTool.getUserByIdFromCache(Integer.valueOf(s));
            String[] roles = user.getJurisdiction().split(",");
            GrantedAuthority grantedAuthority = null;
            for (int i=0;i<roles.length;i++){
                grantedAuthority = new SimpleGrantedAuthority(roles[i]);
                grantedAuthorities.add(grantedAuthority);
            }
            return new org.springframework.security.core.userdetails.User(
                    String.valueOf(user.getUser_id()),user.getPassword(),grantedAuthorities);
        }
        return null;
    }
}
