package com.example.oauth2_server.config.entity;

import com.example.oauth2_server.entity.Customer;
import com.example.oauth2_server.entity.Staff;
import com.example.oauth2_server.mapper.CustomerMapper;
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
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired(required = false)
    CustomerMapper customerMapper;

    @Autowired(required = false)
    StaffMapper staffMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (customerMapper.findCustomerById(s)!=null){
            Customer customer = customerMapper.findCustomerById(s);
            String[] role = customer.getCustomer_role().split(",");
            for (int i=0;i<role.length;i++){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role[i]);
                grantedAuthorities.add(grantedAuthority);
            }
            return new User(customer.getCustomer_account(),customer.getCustomer_password(),grantedAuthorities);
        }else if (staffMapper.findStaffById(Integer.parseInt(s))!=null){
            Staff staff = staffMapper.findStaffById(Integer.parseInt(s));
            String[] position = staff.getStaff_position().split(",");
            for (int i=0;i<position.length;i++){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(position[i]);
                grantedAuthorities.add(grantedAuthority);
            }
            return new User(String.valueOf(staff.getStaff_id()),staff.getStaff_name(),grantedAuthorities);
        }
        return new User(null,null,null);
    }
}
