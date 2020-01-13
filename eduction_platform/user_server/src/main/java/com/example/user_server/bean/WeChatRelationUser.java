package com.example.user_server.bean;

import lombok.Data;

@Data
public class WeChatRelationUser {
    private String id;
    private int invitation_id;

    public WeChatRelationUser(){
    }

    public WeChatRelationUser(String id, int invitation_id){
        this.id = id;
        this.invitation_id =invitation_id;
    }
}
