package com.example.business_server.controller;

import com.example.business_server.entity.Staff;
import com.example.business_server.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StaffController {

    @Autowired
    StaffService staffService;

    @RequestMapping
    public String findAnyStaff(Model model,long number){
        model.addAttribute("staffList",staffService.findAnyStaff(number));
        return "";
    }

    public String updateStaffPosition(int staff_id,String staff_position){
        staffService.updateStaffPosition(staff_id,staff_position);
        return "";
    }

    public String addStaff(Staff staff){
        staffService.addStaff(staff);
        return "";
    }

    public String delete(int staff_id){
        staffService.deleteStaff(staff_id);
        return "";
    }
}
