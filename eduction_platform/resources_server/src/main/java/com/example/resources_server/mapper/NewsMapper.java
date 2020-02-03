package com.example.resources_server.mapper;

import com.example.resources_server.entity.Book;
import com.example.resources_server.entity.BookSqlProvider;
import com.example.resources_server.entity.News;
import com.example.resources_server.entity.NewsSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface NewsMapper {
    /**
     *根据id,获取新闻的所有信息
     * @param id
     * @return
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public News getNewsAllInformationById(String id);

    /**
     * 根据id获取在页面展示的基本信息
     * @param id
     * @return
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public News getNewsShowBaseInformationById(String id);

    /**
     * 根据id获取在页面展示的详细信息
     * @param id
     * @return
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public News getNewsShowDetailedInformationById(String id);

    /**
     * 根据标题headline获取在页面展示的基本信息
     * @param headline
     * @return
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public List<News> getNewsShowBaseInformationByHeadline(String headline, int index);

    /**
     * 根据新闻类型type获取在页面展示的基本信息
     * @param file_type
     * @return
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public List<News> getNewsShowBaseInformationByType(short file_type);

    /**
     * 根据上传者id获取在页面展示的基本信息
     * @param upload_id
     * @return
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public List<News> getNewsShowBaseInformationByUpload_Id(int upload_id);

    /**
     * 添加新闻
     * @param news
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public void addNews(News news);

    /**
     * 根据id删除新闻
     * @param id
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public void deleteNewsById(String id);

    /**
     * 修改新闻信息
     * @param news
     */
    @SelectProvider(value = NewsSqlProvider.class,method = "")
    public void updateNews(News news);
}
