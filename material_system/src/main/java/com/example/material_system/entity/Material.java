package com.example.material_system.entity;

import lombok.Data;

import java.sql.Date;

@Data
public class Material {
    private String material_md5_id;
    private String material_upload_college;
    private Date upload_time;
    private String material_name;
    private String material_upload_department;
}
