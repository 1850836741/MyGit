package com.example.material_system.config;

import com.example.material_system.entity.College;
import com.example.material_system.mapper.CollegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired(required = false)
    CollegeMapper collegeMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>(0);
        College college = collegeMapper.getCollegeLoginInformation(Integer.parseInt(s));
        if (college!=null){
            String[] roles = college.getCollege_authority().split(",");
            GrantedAuthority grantedAuthority = null;
            for (int i=0;i<roles.length;i++){
                grantedAuthority=new SimpleGrantedAuthority(roles[i]);
                grantedAuthorityList.add(grantedAuthority);
            }
            return new User(String.valueOf(college.getCollege_account()),
                    bCryptPasswordEncoder.encode(college.getCollege_password()),
                    grantedAuthorityList);
        }
        return null;
    }
}
