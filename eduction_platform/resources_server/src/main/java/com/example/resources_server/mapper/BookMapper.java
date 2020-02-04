package com.example.resources_server.mapper;

import com.example.resources_server.entity.Book;
import com.example.resources_server.entity.BookSqlProvider;
import com.example.resources_server.entity.VideoSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface BookMapper {
    /**
     *根据id,获取书籍的所有信息
     * @param id
     * @return
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public Book getBookAllInformationById(String id);

    /**
     * 根据id获取在页面展示的基本信息
     * @param id
     * @return
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public Book getBookShowBaseInformationById(String id);

    /**
     * 根据id获取在页面展示的详细信息
     * @param id
     * @return
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public Book getBookShowDetailedInformationById(String id);

    /**
     * 根据视频id获取上传者的账号
     * @param id
     * @return
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public int getBookUploadIdById(String id);

    /**
     * 根据标题headline获取在页面展示的基本信息
     * @param headline
     * @return
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public List<Book> getBooksShowBaseInformationByHeadline(String headline, int index);

    /**
     * 根据书籍类型type获取在页面展示的基本信息
     * @param file_type
     * @return
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public List<Book> getBooksShowBaseInformationByType(String file_type,int count);

    /**
     * 根据上传者id获取在页面展示的基本信息
     * @param upload_id
     * @return
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public List<Book> getBooksShowBaseInformationByUpload_Id(int upload_id);

    /**
     * 添加书籍
     * @param book
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public void addBook(Book book);

    /**
     * 根据id删除书籍
     * @param id
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public void deleteBookById(String id);

    /**
     * 修改书籍信息
     * @param book
     */
    @SelectProvider(value = BookSqlProvider.class,method = "")
    public void updateBook(Book book);
}
