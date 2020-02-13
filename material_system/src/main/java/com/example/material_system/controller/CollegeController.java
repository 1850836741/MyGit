package com.example.material_system.controller;

import com.example.material_system.entity.College;
import com.example.material_system.mapper.CollegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class CollegeController {

    @Autowired(required = false)
    CollegeMapper collegeMapper;

    @Autowired
    ConcurrentHashMap<String,String> concurrentHashMap;

    /**
     * 获取所有除自己之外学院的名字
     * @return
     */
    @GetMapping(value = "/admin/getAllCollege")
    public String getAllCollege(HttpServletRequest httpServletRequest, Model model){
        int college_account = Integer.parseInt((String) httpServletRequest.getSession().getAttribute("account"));
        List<College> collegeList = collegeMapper.getAllCollege(college_account);
        model.addAttribute("collegeList",collegeList);
        return "/allCollege";
    }

    /**
     * 通知学院
     * @param college_account
     * @param notice
     * @param model
     * @return
     */
    @PostMapping(value = "/admin/noticeCollege")
    public String noticeCollege(int college_account, String notice,Model model){
        concurrentHashMap.put(String.valueOf(college_account),notice);
        List<College> collegeList = collegeMapper.getAllCollege(college_account);
        model.addAttribute("collegeList",collegeList);
        return "/allCollege";
    }

    /**
     * 去往添加通知的界面
     * @param model
     * @return
     */
    @GetMapping(value = "/admin/toNoticeCollege")
    public String toNoticeCollege(int college_account,Model model){
        model.addAttribute("college_account",college_account);
        return "/addNotice";
    }

    /**
     * 前往管理界面
     * @return
     */
    @GetMapping(value = "/admin/adminIndex")
    public String adminIndex(){
        return "/adminIndex";
    }

    /**
     * 前往添加用户的界面
     * @return
     */
    @GetMapping(value = "/admin/toAddCollege")
    public String toAddCollege(){
        return "/addCollege";
    }


    /**
     * 添加学院账号
     * @param college
     * @return
     */
    @PostMapping(value = "/admin/addCollege")
    public String addCollege(College college){
        if (college.getCollege_name()==null||college.getCollege_name().trim()==""){
            college.setCollege_name("计算机与信息工程学院");
        }
        collegeMapper.addCollege(college);
        return "redirect:/admin/toAddCollege";
    }

    /**
     * 删除学院
     * @param college_account
     * @param model
     * @return
     */
    @GetMapping(value = "/admin/deleteCollege")
    public String deleteCollege(int college_account,Model model){
        collegeMapper.deleteCollege(college_account);
        return "redirect:/admin/getAllCollege";
    }
}
