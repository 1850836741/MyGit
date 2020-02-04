package com.example.resources_server.mapper;

import com.example.resources_server.entity.Video;
import com.example.resources_server.entity.VideoSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;
import java.util.List;

/**
 * 视频的dao层方法
 */
public interface VideoMapper {

    /**
     *根据id,获取学习资料的所有信息
     * @param id
     * @return
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public Video getVideoAllInformationById(String id);

    /**
     * 根据id获取在页面展示的基本信息
     * @param id
     * @return
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public Video getVideoShowBaseInformationById(String id);

    /**
     * 根据id获取在页面展示的详细信息
     * @param id
     * @return
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public Video getVideoShowDetailedInformationById(String id);

    /**
     * 根据标题headline获取在页面展示的基本信息
     * @param headline
     * @return
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public List<Video> getVideosShowBaseInformationByHeadline(String headline,int index);

    /**
     * 根据视频类型type获取在页面展示的基本信息
     * @param file_type
     * @return
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public List<Video> getVideosShowBaseInformationByType(String file_type,int count);

    /**
     * 根据上传者id获取在页面展示的基本信息
     * @param upload_id
     * @return
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public List<Video> getVideosShowBaseInformationByUpload_Id(int upload_id);

    /**
     * 根据视频id获取上传者的账号
     * @param id
     * @return
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public int getVideoUploadIdById(String id);

    /**
     * 添加视频
     * @param video
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public void addVideo(Video video);

    /**
     * 根据id删除视频
     * @param id
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public void deleteVideoById(String id);

    /**
     * 修改视频信息
     * @param video
     */
    @SelectProvider(value = VideoSqlProvider.class,method = "")
    public void updateVideo(Video video);
}
