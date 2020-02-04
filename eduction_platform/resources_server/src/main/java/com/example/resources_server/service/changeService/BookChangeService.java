package com.example.resources_server.service.changeService;

import com.example.resources_server.entity.Book;
import com.example.resources_server.mapper.BookMapper;
import com.example.resources_server.r_cache.RedisCacheConfig;
import com.example.resources_server.r_cache.RedisTool;
import com.example.resources_server.service.selectService.BookSelectService;
import com.example.resources_server.tool.BloomFilter;
import com.example.resources_server.tool.CloseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 书籍增删改服务
 */
@Service
public class BookChangeService {
    @Autowired
    RedisTool<Book> redisTool;

    @Autowired
    BloomFilter bloomFilter;

    @Autowired(required = false)
    BookMapper bookMapper;

    @Autowired
    BookSelectService bookSelectService;

    /**
     * 删除视频
     * @param user_id
     * @param bookId
     */
    public void deleteBook(int user_id,String bookId){
        if (bookSelectService.getBookUploadIdById(bookId) == user_id){
            redisTool.deleteCacheByKey(bookId);
            redisTool.deleteWithHash(RedisCacheConfig.BOOK_MD5_HOT_CACHE_HASH,bookId);
            bookMapper.deleteBookById(bookId);
        }
    }

    /**
     * 修改视频信息
     * @param user_id
     * @param book
     */
    public void updateBook(int user_id,Book book){
        if (bookSelectService.getBookUploadIdById(book.getId()) == user_id){
            //删除缓存中存储的信息
            redisTool.deleteCacheByKey(book.getId());
            //删除hotBooks中的md5标识
            redisTool.deleteWithHash(RedisCacheConfig.BOOK_MD5_HOT_CACHE_HASH,book.getId());
            bookMapper.updateBook(book);
        }
    }

    /**
     * 向审批hash中存放video信息
     * @param book
     */
    public void addBookToApproval(Book book){
        redisTool.addToHash(RedisCacheConfig.PENDING_APPROVAL_BOOK, book.getId(), book);
    }

    /**
     * 将审批过后的视频存储添加进数据库
     * @param bookId
     */
    public void addBook(String bookId){
        Book book = redisTool.selectObjectByHash(RedisCacheConfig.PENDING_APPROVAL_BOOK,bookId,Book.class);
        redisTool.deleteWithHash(RedisCacheConfig.PENDING_APPROVAL_BOOK,bookId);
        bookMapper.addBook(book);
        bloomFilter.setInBloomSet(bookId);
        bloomFilter.setInBloomSet(book.getHeadline());
    }

    /**
     * 通过上传的视频的md5值来确定该视频是否存在
     * @param md5File
     * @return
     */
    public boolean checkFile(String md5File){
        Boolean exist = false;
        //判断文件是否曾经上传过
        if (bookSelectService.isExist(md5File)){
            exist = true;
        }
        return exist;
    }

    /**
     * 检查分片是否存在
     * @param md5File
     * @param chunk
     * @return
     */
    public Boolean checkChunk(String md5File, Integer chunk) {
        Boolean exist = false;
        StringBuilder path = new StringBuilder(RedisCacheConfig.TEMPORARY_FILE_PATH);
        path.append(md5File).append("/");//分片存放目录
        String chunkName = String.valueOf(chunk).concat(".tmp");//分片名
        File file = new File(path.toString().concat(chunkName));
        if (file.exists()) {
            exist = true;
        }
        return exist;
    }

    /**
     * 分片上传
     * @param file
     * @param md5File
     * @param chunk
     * @return
     */
    public Boolean upload(MultipartFile file, String md5File, Integer chunk) { //第几片，从0开始
        StringBuilder path = new StringBuilder(RedisCacheConfig.TEMPORARY_FILE_PATH);
        path.append(md5File).append("/");//分片存放目录
        File dirFile = new File(path.toString());
        if (!dirFile.exists()) {//目录不存在，创建目录
            dirFile.mkdirs();
        }
        String chunkName;
        if(chunk == null) {//表示是小文件，还没有一片
            chunkName = "0.tmp";
        }else {
            chunkName = String.valueOf(chunk).concat(".tmp");
        }
        String filePath = path.toString().concat(chunkName);
        File savefile = new File(filePath);

        try {
            if (!savefile.exists()) {
                savefile.createNewFile();//文件不存在，则创建
            }
            file.transferTo(savefile);//将文件保存
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 合并分片
     * @param chunks
     * @param md5File
     * @param name
     * @return
     * @throws Exception
     */
    public Boolean  merge(Integer chunks, String md5File, String name) throws Exception {
        String path = RedisCacheConfig.BOOK_STORAGE_PATH;
        FileOutputStream fileOutputStream = new FileOutputStream(path.concat(name));  //合成后的文件
        try {
            byte[] buf = new byte[1024];
            for(long i=0;i<chunks;i++) {
                String chunkFile=String.valueOf(i).concat(".tmp");
                File file = new File(RedisCacheConfig.TEMPORARY_FILE_PATH.concat(md5File).concat("/").concat(chunkFile));
                InputStream inputStream = new FileInputStream(file);
                int len = 0;
                while((len=inputStream.read(buf))!=-1){
                    fileOutputStream.write(buf,0,len);
                }
                CloseTool.Close(inputStream);
            }
            //合并完，要删除md5目录及临时文件，节省空间。
            File file = new File(RedisCacheConfig.TEMPORARY_FILE_PATH.concat(md5File));
            deleteFile(file);
        } catch (Exception e) {
            return false;
        }finally {
            fileOutputStream.close();
        }
        return true;
    }

    /**
     * 删除指定文件
     * @param file
     */
    public void deleteFile(File file){
        File[] files = file.listFiles();
        for (File f:files){
            if (f.isDirectory()){
                deleteFile(f);
            }else {
                f.delete();
            }
        }
        file.delete();
    }
}
