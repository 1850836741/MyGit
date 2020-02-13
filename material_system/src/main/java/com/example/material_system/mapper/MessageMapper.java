package com.example.material_system.mapper;

import com.example.material_system.entity.Message;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MessageMapper {

    @Insert("INSERT INTO message(message_id,message_name,message_context,creat_time) VALUES(#{message_id},#{message_name},#{message_context},#{creat_time})")
    void addMessage(Message message);

    @Select("SELECT message_id,message_name,message_context,creat_time FROM message ORDER BY creat_time desc LIMIT #{start},#{end}")
    List<Message> getAllMessage(@Param(value = "start") int start,@Param(value = "end") int end);

    @Select("SELECT message_id,message_name,message_context,creat_time FROM message ORDER BY creat_time desc")
    List<Message> getAllMessageNotLimit();

    @Select("SELECT message_id,message_name,message_context,creat_time FROM message ORDER BY creat_time desc LIMIT 0,10")
    List<Message> getNewMessage();

    @Select("SELECT COUNT(message_id) FROM message")
    int count();

    @Select("SELECT message_id,message_name,message_context,creat_time FROM message WHERE message_id = #{message_id}")
    Message getMessageById(@Param(value = "message_id") int message_id);

    @Delete("DELETE FROM message WHERE message_id = #{message_id}")
    void deleteMessageById(int message_id);
}
