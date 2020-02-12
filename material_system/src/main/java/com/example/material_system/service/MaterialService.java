package com.example.material_system.service;

import com.example.material_system.config.ConstantConfig;
import com.example.material_system.entity.Material;
import com.example.material_system.mapper.CollegeMapper;
import com.example.material_system.mapper.MaterialMapper;
import com.example.material_system.util.CloseUtil;
import com.example.material_system.util.DateUtil;
import com.example.material_system.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;

@Service
public class MaterialService {

    @Autowired(required = false)
    MaterialMapper materialMapper;

    @Autowired
    Executor executor;

    @Autowired(required = false)
    CollegeMapper collegeMapper;

    /**
     * 通过上传的文件的md5值来确定该视频是否存在
     * @param md5File
     * @return
     */
    public boolean checkFile(String md5File){
        Boolean exist = false;
        //判断文件是否曾经上传过
        if (materialMapper.judgeIsExist(md5File)!=null){
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
        StringBuilder path = new StringBuilder(ConstantConfig.TEMPORARY_PATH);
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
        StringBuilder path = new StringBuilder(ConstantConfig.TEMPORARY_PATH);
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
    public Boolean  merge(Integer chunks, String md5File, String name, String collegeName, String material_upload_department) throws Exception {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String path = ConstantConfig.FILE_PATH;
                File pack = new File(path.concat(material_upload_department));
                String pictureName = name.substring(0,name.lastIndexOf(".")).concat(collegeName).concat("-").concat(UUID.randomUUID().toString()).concat(name.substring(name.lastIndexOf("."),name.length()));
                if (!pack.exists()){
                    pack.mkdirs();
                }
                FileOutputStream fileOutputStream =null;
                try {
                    fileOutputStream = new FileOutputStream(path.concat(material_upload_department).concat("\\").concat(pictureName));  //合成后的文件
                    byte[] buf = new byte[1024];
                    InputStream inputStream=null;
                    for(long i=0;i<chunks;i++) {
                        String chunkFile=String.valueOf(i).concat(".tmp");
                        File file = new File(ConstantConfig.TEMPORARY_PATH.concat(md5File).concat("/").concat(chunkFile));
                        if (file.exists()){
                            inputStream = new FileInputStream(file);
                            int len = 0;
                            while((len=inputStream.read(buf))!=-1){
                                fileOutputStream.write(buf,0,len);
                            }
                        }
                        CloseUtil.Close(inputStream);
                    }
                    //合并完，要删除md5目录及临时文件，节省空间。
                    File file = new File(ConstantConfig.TEMPORARY_PATH.concat(md5File));
                    deleteFile(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    CloseUtil.Close(fileOutputStream);
                    Material material = new Material();
                    material.setMaterial_md5_id(md5File);
                    material.setMaterial_name(pictureName);
                    material.setMaterial_upload_college(collegeName);
                    material.setMaterial_upload_department(material_upload_department);
                    material.setUpload_time(new Date(new java.util.Date().getTime()));
                    materialMapper.addMaterialMapper(material);
                }
            }
        });
        return true;
    }

    /**
     * 删除指定文件
     * @param file
     */
    public void deleteFile(File file){
        File[] files = file.listFiles();
        if (files!=null){
            for (File f:files){
                if (f.isDirectory()){
                    deleteFile(f);
                }else {
                    f.delete();
                }
            }
        }
        file.delete();
    }

    /**
     * 获取指定页的文件信息
     * @param index
     * @return
     */
    public List<Material> selectAllMaterial(int index,String material_upload_department){
        return materialMapper.getAllMaterialInformation((index-1)* ConstantConfig.NUMBER,index*ConstantConfig.NUMBER,material_upload_department);
    }

    /**
     * 获取文件总数
     * @return
     */
    public int count(){
        return materialMapper.count();
    }

    /**
     * 查询自己上传的文件
     * @param material_upload_college
     * @return
     */
    public List<Material> selectMaterialByCollege(String material_upload_college){
        return materialMapper.getMaterialInformationByCollegeName(material_upload_college);
    }

    /**
     * 删除自己上传的文件
     * @param material_md5_id
     */
    public void deleteMaterialById(String material_md5_id){
        Material material = materialMapper.getMaterialById(material_md5_id);
        materialMapper.deleteMaterial(material_md5_id);
        String pictureName = material.getMaterial_name();
        String material_upload_department= material.getMaterial_upload_department();
        File file = new File(ConstantConfig.FILE_PATH.concat(material_upload_department).concat("\\").concat(pictureName));
        executor.execute(new Runnable() {
            @Override
            public void run() {
                deleteFile(file);
            }
        });
    }


    /**
     * 下载单个文件
     * @param httpServletResponse
     * @param material_md5_id
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean downLoad(HttpServletResponse httpServletResponse,String material_md5_id) throws UnsupportedEncodingException {
        Material material = materialMapper.getMaterialById(material_md5_id);
        String filePath = ConstantConfig.FILE_PATH.concat(material.getMaterial_upload_department()).concat("\\").concat(material.getMaterial_name());
        File file = new File(filePath);
        if (file.exists()){
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(material.getMaterial_name(),"UTF-8"));
            byte[] buffer = new byte[1024];
            FileInputStream fileInputStream = null;
            OutputStream outputStream = null;
            try {
                outputStream = httpServletResponse.getOutputStream();
                fileInputStream = new FileInputStream(file);
                int i = 0;
                while ((i=fileInputStream.read(buffer))!=-1){
                    outputStream.write(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                CloseUtil.Close(1,fileInputStream,outputStream);
            }
        }
        return true;
    }

    /**
     * 生成excel报表
     */
    public void creatExcel(){
        Map<String,List<Material>> departmentMaterialMap = new HashMap<>();
        List<String> departmentName = materialMapper.getAllDepartmentName();
        departmentName.stream().forEach(department->{
            departmentMaterialMap.put(department,materialMapper.creatExcel(department));
        });
        ExcelUtil.creatExcel(departmentMaterialMap,ConstantConfig.EXCEL_TITLE);
    }
}
