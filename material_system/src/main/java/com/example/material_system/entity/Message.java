package com.example.material_system.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Data

public class Message {
    private int message_id;

    @NotEmpty(message = "文章标题不能为空")
    @Length(min = 1,max = 100,message = "标题要控制在1~100个字符")
    private String message_name;

    @NotEmpty(message = "文章内容不能为空")
    @Length(min = 1,max = 2147483647,message = "内容要控制在1~2147483647个字符")
    private String message_context;
    private Date creat_time;
}
