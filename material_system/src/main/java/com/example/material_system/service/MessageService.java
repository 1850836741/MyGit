package com.example.material_system.service;

import com.example.material_system.config.ConstantConfig;
import com.example.material_system.entity.Message;
import com.example.material_system.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired(required = false)
    MessageMapper messageMapper;

    /**
     * 获取最新消息
     * @return
     */
    public List<Message> getNewMessage(){
        return messageMapper.getNewMessage();
    }

    /**
     * 添加信息
     * @param message
     */
    public void addMessage(Message message){
        message.setCreat_time(new Date(new java.util.Date().getTime()));
        messageMapper.addMessage(message);
    }

    /**
     * 获取所有信息(最大为200份)
     * @param index
     * @return
     */
    public List<Message> getAllMessage(int index){
        return messageMapper.getAllMessage((index-1)* ConstantConfig.NUMBER,index*ConstantConfig.NUMBER);
    }

    /**
     * 获取信息数量
     * @return
     */
    public int count(){
        return messageMapper.count();
    }

    /**
     * 通过id获取消息
     * @param message_id
     * @return
     */
    public Message getMessageById(int message_id){
        return messageMapper.getMessageById(message_id);
    }
}
