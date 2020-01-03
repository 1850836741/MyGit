package com.example.oauth2_server.config.entity;

import com.example.oauth2_server.entity.Staff;
import com.example.oauth2_server.mapper.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired(required = false)
    StaffMapper staffMapper;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(1);
        if (staffMapper.findStaffById(Integer.parseInt(s))!=null){
            Staff staff = staffMapper.findStaffById(Integer.parseInt(s));
            String roles[] = staff.getStaff_position().split(",");
            Arrays.stream(roles).forEach(x->{
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(x);
                grantedAuthorities.add(grantedAuthority);
            });
            return new User(String.valueOf(staff.getStaff_id()),bCryptPasswordEncoder.encode(staff.getStaff_idcard()),grantedAuthorities);
        }
        return new User(null,null,null);
    }
}
