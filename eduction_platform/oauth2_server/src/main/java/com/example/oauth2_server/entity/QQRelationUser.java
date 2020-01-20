package com.example.oauth2_server.entity;

import lombok.Data;

@Data
public class QQRelationUser {
    private String id;
    private int invitation_id;
    private User user;

    public QQRelationUser(){
    }

    public QQRelationUser(String id, int invitation_id){
        this.id = id;
        this.invitation_id =invitation_id;
    }
}
