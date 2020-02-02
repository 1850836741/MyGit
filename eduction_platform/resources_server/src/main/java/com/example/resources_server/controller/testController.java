package com.example.resources_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
public class testController {

    @RequestMapping(value = "/")
    public String tst(){
        return "upload";
    }



    @PostMapping("/checkFile")
    @ResponseBody
    public Boolean checkFile(@RequestParam(value = "md5File") String md5File) {
        Boolean exist = false;

        //实际项目中，这个md5File唯一值，应该保存到数据库或者缓存中，通过判断唯一值存不存在，来判断文件存不存在，这里我就不演示了
		/*if(true) {
			exist = true;
		}*/
		System.out.println("检查md5是否存在");
        return exist;
    }

    /**
     * @author van
     * 检查分片存在与否
     */
    @PostMapping("/checkChunk")
    @ResponseBody
    public Boolean checkChunk(@RequestParam(value = "md5File") String md5File,
                              @RequestParam(value = "chunk") Integer chunk) {
        Boolean exist = false;
        String path = "E:/"+md5File+"/";//分片存放目录
        String chunkName = chunk+ ".tmp";//分片名
        File file = new File(path+chunkName);
        if (file.exists()) {
            exist = true;
        }
        System.out.println("检查分片是否存在");
        return exist;
    }

    /**
     * @author van
     * 上传，这里根据文件md5值生成目录，并将分片文件放到该目录下
     */
    @PostMapping("/upload")
    @ResponseBody
    public Boolean upload(@RequestParam(value = "file") MultipartFile file,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "chunk",required= false) Integer chunk) { //第几片，从0开始
        String path = "E:/"+md5File+"/";
        File dirfile = new File(path);
        if (!dirfile.exists()) {//目录不存在，创建目录
            dirfile.mkdirs();
        }
        String chunkName;
        if(chunk == null) {//表示是小文件，还没有一片
            chunkName = "0.tmp";
        }else {
            chunkName = chunk+ ".tmp";
        }
        String filePath = path+chunkName;
        File savefile = new File(filePath);

        try {
            if (!savefile.exists()) {
                savefile.createNewFile();//文件不存在，则创建
            }
            file.transferTo(savefile);//将文件保存
        } catch (IOException e) {
            return false;
        }
        System.out.println("生成子分片"+chunk);
        return true;
    }

    /**
     * @author van
     * 合成分片
     */
    @PostMapping("/merge")
    @ResponseBody
    public Boolean  merge(@RequestParam(value = "chunks",required =false) Integer chunks,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "name") String name) throws Exception {
        String path = "E:";
        FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+name);  //合成后的文件
        try {
            byte[] buf = new byte[1024];
            for(long i=0;i<chunks;i++) {
                String chunkFile=i+".tmp";
                File file = new File(path+"/"+md5File+"/"+chunkFile);
                InputStream inputStream = new FileInputStream(file);
                int len = 0;
                while((len=inputStream.read(buf))!=-1){
                    fileOutputStream.write(buf,0,len);
                }
                inputStream.close();
            }
            //合并完，要删除md5目录及临时文件，节省空间。这里代码省略

        } catch (Exception e) {
            return false;
        }finally {
            fileOutputStream.close();
        }
        System.out.println("合并了"+chunks);
        return true;
    }
}
