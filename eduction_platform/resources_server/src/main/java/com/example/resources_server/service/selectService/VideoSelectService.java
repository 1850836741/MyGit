package com.example.resources_server.service.selectService;

import com.alibaba.fastjson.JSON;
import com.example.resources_server.entity.Video;
import com.example.resources_server.mapper.VideoMapper;
import com.example.resources_server.r_cache.RedisCacheConfig;
import com.example.resources_server.r_cache.RedisTool;
import com.example.resources_server.tool.BloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class VideoSelectService {

    @Autowired
    BloomFilter bloomFilter;

    @Autowired
    RedisTool<Video> redisTool;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired(required = false)
    VideoMapper videoMapper;

    /**
     * 获取视频上传者Id
     * @param videoId
     * @return
     */
    public Integer getVideoUploadIdById(String videoId){
        if (bloomFilter.isExist(videoId)){
            if (stringRedisTemplate.opsForValue().get(videoId).equals("empty")) return null;
            Video video = redisTool.selectObject(videoId,Video.class);
            if (video == null){
                int user_id = videoMapper.getVideoUploadIdById(videoId);
                if (user_id <= 100000 || user_id >= Integer.MAX_VALUE){
                    redisTool.addEmptyWithLimit(videoId,6, TimeUnit.SECONDS);
                    return null;
                }
                return user_id;
            }
            return video.getUpload_id();
        }
        return null;
    }

    /**
     * 根据id获取video的全部信息
     * @param videoId
     * @return
     */
    public Video getVideoById(String videoId){
        if (bloomFilter.isExist(videoId)){
            if (stringRedisTemplate.opsForValue().get(videoId).equals("empty")) return null;
            Video video = redisTool.selectObject(videoId,Video.class);
            if (video == null){
                video = videoMapper.getVideoAllInformationById(videoId);
                if (video == null){
                    redisTool.addEmptyWithLimit(videoId,6, TimeUnit.SECONDS);
                    return null;
                }
            }
            return video;
        }
        return null;
    }

    /**
     * 根据id获取video在页面展示的基本信息
     * @param videoId
     * @return
     */
    public Video getVideoShowBaseInformationById(String videoId){
        if (bloomFilter.isExist(videoId)){
            if (stringRedisTemplate.opsForValue().get(videoId).equals("empty")) return null;
            Video video = redisTool.selectObject(videoId,Video.class);
            if (video == null){
                video = videoMapper.getVideoShowBaseInformationById(videoId);
                if (video == null){
                    redisTool.addEmptyWithLimit(videoId,6, TimeUnit.SECONDS);
                    return null;
                }
            }
            return video;
        }
        return null;
    }

    /**
     * 根据id获取在页面展示的详细信息
     * @param videoId
     * @return
     */
    public Video getVideoShowDetailedInformationById(String videoId){
        if (bloomFilter.isExist(videoId)){
            if (stringRedisTemplate.opsForValue().get(videoId).equals("empty")) return null;
            Video video = redisTool.selectObject(videoId,Video.class);
            if (video == null){
                video = videoMapper.getVideoShowDetailedInformationById(videoId);
                if (video == null){
                    redisTool.addEmptyWithLimit(videoId,6, TimeUnit.SECONDS);
                    return null;
                }
            }
            return video;
        }
        return null;
    }

    /**
     * 根据标题headline获取在页面展示的基本信息
     * @param videoHeadLine
     * @param searchCondition
     * @param count
     * @return
     */
    public Set<Video> getVideosShowBaseInformationByHeadline(String videoHeadLine, List<String> searchCondition, int count){
        if (stringRedisTemplate.opsForValue().get(videoHeadLine).equals("empty")) return null;
        Set<Video> videoSet = new HashSet<>();
        searchCondition.stream().forEach(condition->{
            try {
                Set<String> stringSet = redisTool.scan(condition,videoHeadLine,count);
                for (String videoString:stringSet){
                    videoSet.add(JSON.parseObject(videoString,Video.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //如果缓存中的记录不够，再查询数据库
        if (videoSet.size()<20){
            videoSet.addAll(videoMapper.getVideosShowBaseInformationByHeadline(videoHeadLine,count));
        }
        if (videoSet.size()<1){
            redisTool.addEmptyWithLimit(videoHeadLine,6, TimeUnit.SECONDS);
            return null;
        }
        return videoSet;
    }

    /**
     * 获取相应类型的视频
     * @param searchCondition
     * @param count
     * @return
     */
    public Set<Video> getVideoShowBaseInformationByType(List<String> searchCondition, int count){
        Set<Video> videoSet = new HashSet<>();
        searchCondition.stream().forEach(condition->{
            try {
                Set<String> stringSet = redisTool.scan(condition,"*",count);
                for (String videoString:stringSet){
                    videoSet.add(JSON.parseObject(videoString,Video.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //缓存不够数据库模糊查询
        if (videoSet.size()<20){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<searchCondition.size(); i++){
                stringBuilder.append(searchCondition.get(i));
            }
            videoSet.addAll(videoMapper.getVideosShowBaseInformationByType(stringBuilder.toString(),count));
        }
        if (videoSet.size()<1)return null;
        return videoSet;
    }


    /**
     *获取上传者id
     * @param upload_Id
     * @return
     */
    public List<Video> getVideoShowBaseInformationByUpload_Id(int upload_Id){
        return videoMapper.getVideosShowBaseInformationByUpload_Id(upload_Id);
    }

    /**
     * 视频是否存在
     * @param id
     * @return
     */
    public boolean isExist(String id){
        if (stringRedisTemplate.opsForHash().hasKey(RedisCacheConfig.VIDEO_MD5_HOT_CACHE_HASH,id)){
            return true;
        }else if (videoMapper.getVideoUploadIdById(id)<=100000 || videoMapper.getVideoUploadIdById(id)>=Integer.MAX_VALUE){
            return true;
        }
        return false;
    }

}
