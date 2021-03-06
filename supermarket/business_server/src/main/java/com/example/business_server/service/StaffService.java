package com.example.business_server.service;

import com.example.business_server.entity.Staff;
<<<<<<< HEAD
import com.example.business_server.entity.StaffOrder;
import com.example.business_server.mapper.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
=======
import com.example.business_server.mapper.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
public class StaffService {

    @Autowired(required = false)
    StaffMapper staffMapper;

<<<<<<< HEAD
    @RequestMapping(value = "/getStaffById",method = RequestMethod.GET)
    public Staff getStaffById(String staff_order_id){
        return staffMapper.findStaffById(staff_order_id);
    }

    @RequestMapping(value = "/getStaff",method = RequestMethod.GET)
    public List<Staff> getStaff(){
        return staffMapper.findStaff(20);
    }

    @RequestMapping(value = "/getStaffOrderById",method = RequestMethod.GET)
    public List<StaffOrder> getStaffOrderById(String staff_order_id){
        return staffMapper.findStaffOrderById(staff_order_id);
    }

    @RequestMapping(value = "/getStaffOrderWithTime",method = RequestMethod.GET)
    public List<StaffOrder> getStaffOrderWithTime(String staff_salary_time){
        if (staff_salary_time==null||staff_salary_time.trim().equals("")){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            return staffMapper.findStaffOrderWithTime(simpleDateFormat.format(date).substring(0,10));
        }
        return staffMapper.findStaffOrderWithTime(staff_salary_time);
    }

    @RequestMapping(value = "/addStaff",method = RequestMethod.POST)
    public void addStaff(@RequestBody Staff staff){
        staffMapper.addStaff(staff);
    }

    @RequestMapping(value = "/addStaffOrder",method = RequestMethod.POST)
    public void addStaffOrder(@RequestBody StaffOrder staffOrder){
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uuid = UUID.randomUUID().toString();
            staffOrder.setStaff_order_id(uuid);
            staffOrder.setStaff_salary_time(simpleDateFormat.format(date));
            staffMapper.addStaffOrder(staffOrder);
    }

    @RequestMapping(value = "/deleteStaff",method = RequestMethod.GET)
    public void deleteStaff(String staff_id){
        staffMapper.deleteStaff(staff_id);
    }


    @RequestMapping(value = "/deleteStaffOrder",method = RequestMethod.GET)
    public void deleteStaffOrder(String staff_order_id){
        staffMapper.deleteStaffOrder(staff_order_id);
    }
=======
    /**
     * 查找一定数量的职员信息
     * @param number
     * @return
     */
    public List<Staff> findAnyStaff(long number){
        return staffMapper.findAnyStaff(number);
    }

    /**
     * 修改职位
     * @param staff_id
     * @param staff_position
     */
    public void updateStaffPosition(int staff_id,String staff_position){
        staffMapper.setStaffHabitation(staff_id,staff_position);
    }

    /**
     * 修改居住地
     * @param staff_id
     * @param staff_habitation
     */
    public void updateStaffHabitation(int staff_id,String staff_habitation){
        staffMapper.setStaffHabitation(staff_id,staff_habitation);
    }

    /**
     * 添加职员
     * @param staff
     */
    public void addStaff(Staff staff){
        staffMapper.addStaff(staff);
    }

    /**
     * 删除职员
     * @param staff_id
     */
    public void deleteStaff(int staff_id){
        staffMapper.deleteStaff(staff_id);
    }
>>>>>>> 1f928a1d30a9d70d31fe1f06a3e043248d2a6984
}
