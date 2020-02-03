package com.example.resources_server.service.changeService;

import com.example.resources_server.entity.Video;
import com.example.resources_server.mapper.VideoMapper;
import com.example.resources_server.r_cache.RedisTool;
import com.example.resources_server.service.selectService.VideoSelectService;
import com.example.resources_server.tool.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoChangeService {

    @Autowired
    RedisTool<Video> redisTool;

    @Autowired
    BloomFilter bloomFilter;

    @Autowired(required = false)
    VideoMapper videoMapper;

    @Autowired
    VideoSelectService videoSelectService;

    /**
     * 删除视频
     * @param user_id
     * @param videoId
     */
    public void deleteVideo(int user_id,String videoId){
        if (videoSelectService.getVideoUploadIdById(videoId) == user_id){
            redisTool.deleteCacheByKey(videoId);
            videoMapper.deleteVideoById(videoId);
        }
    }

    /**
     * 修改视频信息
     * @param user_id
     * @param video
     */
    public void updateVideo(int user_id,Video video){
        if (videoSelectService.getVideoUploadIdById(video.getId()) == user_id){
            redisTool.deleteCacheByKey(video.getId());
            videoMapper.updateVideo(video);
        }
    }
}
