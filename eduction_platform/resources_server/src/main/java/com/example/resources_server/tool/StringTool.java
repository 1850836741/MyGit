package com.example.resources_server.tool;

import org.springframework.stereotype.Component;

/**
 * 解析一些字符串
 */
@Component
public class StringTool {

    /**
     * 解析JSON格式字符串里对应key的值
     * @param context
     * @param key
     * @return
     */
    public static String getWordWithJSON(String context, String key){
        String value = null;
        context = context.substring(context.indexOf(key));
        value = context.substring(key.length()+2,context.indexOf(","));
        return value;
    }
}
