package com.example.oauth2_server.entity;

import lombok.Data;

@Data
public class Staff {
    private int staff_id;
    private String staff_position;
    private String staff_name;
    private int staff_age;
    private String staff_sex;
    private String staff_idcard;
    private String staff_habitation;
    private String staff_native_place;
}
