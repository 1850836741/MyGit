package com.example.material_system.controller;

import com.example.material_system.config.ConstantConfig;
import com.example.material_system.entity.Message;
import com.example.material_system.mapper.CollegeMapper;
import com.example.material_system.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired(required = false)
    CollegeMapper collegeMapper;
    /**
     * 跳转首页
     * @param model
     * @return
     */
    @RequestMapping(value = "/",method = {RequestMethod.GET,RequestMethod.POST})
    public String index(Principal principal, Model model){
        List<Message> messages = messageService.getNewMessage();
        String college = collegeMapper.getCollegeAllInformation(Integer.parseInt(principal.getName())).getCollege_name();
        model.addAttribute("college",college);
        model.addAttribute("messages",messages);
        return "/index";
    }

    /**
     * 前往添加消息界面
     * @return
     */
    @GetMapping(value = "/admin/toAddMessage")
    public String toAddMessage(Model model){
        model.addAttribute("message",new Message());
        return "/addMessage";
    }

    /**
     * 添加消息
     * @param message
     * @return
             */
    @PostMapping(value = "/admin/addMessage")
    public String addMessage(@ModelAttribute("message") @Validated Message message, BindingResult result,Model model){
        if (result.hasErrors()){
            return "/addMessage";
        }
        messageService.addMessage(message);
        model.addAttribute("success","恭喜添加成功");
        return "/addMessage";
    }

    /**
     * 获取所有信息(最大为200份)
     * @param index
     * @return
     */
    @GetMapping(value = "/user/getAllMessage")
    public String getAllMessage(int index, Model model,String material_upload_college){
        List<Message> messages = messageService.getAllMessage(index);
        int count = messageService.count();
        count = count/ ConstantConfig.NUMBER+1;
        count = count>10?10:count;

        List<Integer> numberList = new ArrayList<>();
        for (int i=1;i<=count;i++){
            numberList.add(i);
        }
        model.addAttribute("messages",messages);
        model.addAttribute("numberList",numberList);
        model.addAttribute("college",material_upload_college);
        return "/pastMessage";
    }

    /**
     * 通过id获取消息信息
     * @param message_id
     * @param model
     * @return
     */
    @GetMapping(value = "/user/getMessageById")
    public String getMessageById(int message_id, Model model,String material_upload_college){
        Message message = messageService.getMessageById(message_id);
        model.addAttribute("message",message);
        model.addAttribute("college",material_upload_college);
        return "/messageInformation";
    }
}
