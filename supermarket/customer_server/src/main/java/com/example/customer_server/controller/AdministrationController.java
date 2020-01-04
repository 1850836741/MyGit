package com.example.customer_server.controller;

import com.example.customer_server.entity.Customer;
import com.example.customer_server.entity.Orders;
import com.example.customer_server.entity.SourceOrder;
import com.example.customer_server.entity.Staff;
import com.example.customer_server.mapper.OrderMapper;
import com.example.customer_server.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@SessionAttributes(value = {"customer","shopCart"})
@Controller
public class AdministrationController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CustomerService customerService;

    @Autowired(required = false)
    OrderMapper orderMapper;
    @RequestMapping(value = "/toAdministration",method = RequestMethod.GET)
    public String toAdministration(HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession();
        Customer customer = (Customer) httpSession.getAttribute("customer");
        if (customer==null||!customer.getCustomer_role().contains("ROLE_USER")){
            return "redirect:/toIndex";
        }
        return "Administration";
    }

    @RequestMapping(value = "/aGetOrderByTime",method = RequestMethod.POST)
    public String AGetOrderByTime(String order_time, Model model){
        List<Orders> orders = orderMapper.findOrderWithTime(order_time);
        model.addAttribute("Orders",orders);
        return "Administration";
    }

    @RequestMapping(value = "/AGetSourceByTime",method = RequestMethod.POST)
    public String AGetSourceByTime(Model model,String source_order_time){
        model.addAttribute("sources",new ArrayList<SourceOrder>());
        return "source";
    }

    @RequestMapping(value = "/AAddSource",method = RequestMethod.POST)
    public String AAddSource(){
        return "source";
    }

    @RequestMapping(value = "/AAddSourceInformation",method = RequestMethod.POST)
    public String AAddSourceInformation(){
        return "source";
    }

    @RequestMapping(value = "/ADeleteSource",method = RequestMethod.GET)
    public String ADeleteSource(){
        return "source";
    }

    @RequestMapping(value = "/AtoStaff",method = RequestMethod.GET)
    public String AtoStaff(Model model){
        model.addAttribute("staffs",new ArrayList<Staff>());
        return "staff";
    }

    @RequestMapping(value = "AGetStaffById",method = RequestMethod.POST)
    public String AGetStaffById(String staff_id,Model model){
        model.addAttribute("staffs",new ArrayList<Staff>());
        return "staff";
    }

    @RequestMapping(value = "/ADeleteStaff",method = RequestMethod.GET)
    public String ADeleteStaff(int staff_id){
        return "staff";
    }

    @RequestMapping(value = "/AAddStaff",method = RequestMethod.POST)
    public String AAddStaff(){
        return "staff";
    }

    @RequestMapping(value = "/AGetQuarterly",method = RequestMethod.POST)
    public String AGetQuarterly(String quarterly_time){
        return "finance";
    }

}
