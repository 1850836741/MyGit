package com.example.oauth2_server.config.entity;
import com.example.oauth2_server.entity.User;
import com.example.oauth2_server.mapper.UserMapper;
import com.example.oauth2_server.r_cache.RedisTool;
import com.example.oauth2_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringSecurity用来鉴别对象是否为合法用户的对象
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Autowired
    RedisTool<User> userRedisTool;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (!userRedisTool.limitBehavior(Integer.valueOf(s),10,5)){
            return null;
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();                    //权限对象的ArraysList
        if (userService.getUserByIdFromCache(Integer.valueOf(s))!=null){
            User user = userService.getUserByIdFromCache(Integer.valueOf(s));
            String[] roles = user.getJurisdiction().split(",");
            GrantedAuthority grantedAuthority = null;
            for (int i=0;i<roles.length;i++){
                grantedAuthority = new SimpleGrantedAuthority(roles[i]);
                grantedAuthorities.add(grantedAuthority);
            }
            return new org.springframework.security.core.userdetails.User(
                    String.valueOf(user.getUser_id()),bCryptPasswordEncoder.encode(user.getPassword()),grantedAuthorities);
        }
        return null;
    }
}
