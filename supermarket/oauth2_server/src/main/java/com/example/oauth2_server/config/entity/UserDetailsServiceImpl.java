package com.example.oauth2_server.config.entity;

<<<<<<< HEAD
import com.example.oauth2_server.entity.Staff;
=======
import com.example.oauth2_server.entity.Customer;
import com.example.oauth2_server.entity.Staff;
import com.example.oauth2_server.mapper.CustomerMapper;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
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
<<<<<<< HEAD
import java.util.Arrays;
=======
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

<<<<<<< HEAD
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
=======
    @Autowired(required = false)
    CustomerMapper customerMapper;
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984

    @Autowired(required = false)
    StaffMapper staffMapper;

<<<<<<< HEAD

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
=======
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
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
        }
        return new User(null,null,null);
    }
}
