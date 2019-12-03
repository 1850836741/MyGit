package com.example.business_server.service;

import com.example.business_server.entity.Staff;
import com.example.business_server.mapper.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired(required = false)
    StaffMapper staffMapper;

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
}
