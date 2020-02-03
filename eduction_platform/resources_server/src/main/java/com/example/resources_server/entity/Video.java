package com.example.resources_server.entity;

import lombok.Data;

/**
 * 视频实体类
 */
@Data
public class Video implements LearningMaterials{
    private String id;
    private String headline;
    private short file_type;
    private String file_format;
    private int upload_id;
    private int audit_id;
    private String creation_time;
    private int like_number;
    private int comment_number;

    public Video() {
    }

    public Video(String id, String headline, short file_type, String file_format, int upload_id, int audit_id, String creation_time, int like_number, int comment_number) {
        this.id = id;
        this.headline = headline;
        this.file_type = file_type;
        this.file_format = file_format;
        this.upload_id = upload_id;
        this.audit_id = audit_id;
        this.creation_time = creation_time;
        this.like_number = like_number;
        this.comment_number = comment_number;
    }
}
