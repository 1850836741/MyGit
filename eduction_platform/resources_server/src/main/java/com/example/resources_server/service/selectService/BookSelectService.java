package com.example.resources_server.service.selectService;

import com.alibaba.fastjson.JSON;
import com.example.resources_server.entity.Book;
import com.example.resources_server.mapper.BookMapper;
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
public class BookSelectService {

    @Autowired
    BloomFilter bloomFilter;

    @Autowired
    RedisTool<Book> redisTool;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired(required = false)
    BookMapper bookMapper;

    /**
     * 获取书籍上传者Id
     * @param bookId
     * @return
     */
    public Integer getBookUploadIdById(String bookId){
        if (bloomFilter.isExist(bookId)){
            if (stringRedisTemplate.opsForValue().get(bookId).equals("empty")) return null;
            Book book = redisTool.selectObject(bookId,Book.class);
            if (book == null){
                int user_id = bookMapper.getBookUploadIdById(bookId);
                if (user_id <= 100000 || user_id >= Integer.MAX_VALUE){
                    redisTool.addEmptyWithLimit(bookId,6, TimeUnit.SECONDS);
                    return null;
                }
                return user_id;
            }
            return book.getUpload_id();
        }
        return null;
    }

    /**
     * 根据id获取book的全部信息
     * @param bookId
     * @return
     */
    public Book getBookById(String bookId){
        if (bloomFilter.isExist(bookId)){
            if (stringRedisTemplate.opsForValue().get(bookId).equals("empty")) return null;
            Book book = redisTool.selectObject(bookId,Book.class);
            if (book == null){
                book = bookMapper.getBookAllInformationById(bookId);
                if (book == null){
                    redisTool.addEmptyWithLimit(bookId,6, TimeUnit.SECONDS);
                    return null;
                }
            }
            return book;
        }
        return null;
    }

    /**
     * 根据id获取book在页面展示的基本信息
     * @param bookId
     * @return
     */
    public Book getBookShowBaseInformationById(String bookId){
        if (bloomFilter.isExist(bookId)){
            if (stringRedisTemplate.opsForValue().get(bookId).equals("empty")) return null;
            Book book = redisTool.selectObject(bookId,Book.class);
            if (book == null){
                book = bookMapper.getBookShowBaseInformationById(bookId);
                if (book == null){
                    redisTool.addEmptyWithLimit(bookId,6, TimeUnit.SECONDS);
                    return null;
                }
            }
            return book;
        }
        return null;
    }

    /**
     * 根据id获取在页面展示的详细信息
     * @param bookId
     * @return
     */
    public Book getBookShowDetailedInformationById(String bookId){
        if (bloomFilter.isExist(bookId)){
            if (stringRedisTemplate.opsForValue().get(bookId).equals("empty")) return null;
            Book book = redisTool.selectObject(bookId,Book.class);
            if (book == null){
                book = bookMapper.getBookShowDetailedInformationById(bookId);
                if (book == null){
                    redisTool.addEmptyWithLimit(bookId,6, TimeUnit.SECONDS);
                    return null;
                }
            }
            return book;
        }
        return null;
    }

    /**
     * 根据标题headline获取在页面展示的基本信息
     * @param bookHeadLine
     * @param searchCondition
     * @param count
     * @return
     */
    public Set<Book> getBooksShowBaseInformationByHeadline(String bookHeadLine, List<String> searchCondition, int count){
        if (stringRedisTemplate.opsForValue().get(bookHeadLine).equals("empty")) return null;
        Set<Book> bookSet = new HashSet<>();
        searchCondition.stream().forEach(condition->{
            try {
                Set<String> stringSet = redisTool.scan(condition,bookHeadLine,count);
                for (String bookString:stringSet){
                    bookSet.add(JSON.parseObject(bookString,Book.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //如果缓存中的记录不够，再查询数据库
        if (bookSet.size()<20){
            bookSet.addAll(bookMapper.getBooksShowBaseInformationByHeadline(bookHeadLine,count));
        }
        if (bookSet.size()<1){
            redisTool.addEmptyWithLimit(bookHeadLine,6, TimeUnit.SECONDS);
            return null;
        }
        return bookSet;
    }

    /**
     * 获取相应类型的书籍
     * @param searchCondition
     * @param count
     * @return
     */
    public Set<Book> getBookShowBaseInformationByType(List<String> searchCondition, int count){
        Set<Book> bookSet = new HashSet<>();
        searchCondition.stream().forEach(condition->{
            try {
                Set<String> stringSet = redisTool.scan(condition,"*",count);
                for (String bookString:stringSet){
                    bookSet.add(JSON.parseObject(bookString,Book.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //缓存不够数据库模糊查询
        if (bookSet.size()<20){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i=0; i<searchCondition.size(); i++){
                stringBuilder.append(searchCondition.get(i));
            }
            bookSet.addAll(bookMapper.getBooksShowBaseInformationByType(stringBuilder.toString(),count));
        }
        if (bookSet.size()<1)return null;
        return bookSet;
    }


    /**
     *获取上传者id
     * @param upload_Id
     * @return
     */
    public List<Book> getBooksShowBaseInformationByUpload_Id(int upload_Id){
        return bookMapper.getBooksShowBaseInformationByUpload_Id(upload_Id);
    }

    /**
     * 书籍是否存在
     * @param id
     * @return
     */
    public boolean isExist(String id){
        if (stringRedisTemplate.opsForHash().hasKey(RedisCacheConfig.BOOK_MD5_HOT_CACHE_HASH,id)){
            return true;
        }else if (bookMapper.getBookUploadIdById(id)<=100000 || bookMapper.getBookUploadIdById(id)>=Integer.MAX_VALUE){
            return true;
        }
        return false;
    }

}
