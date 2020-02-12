package com.example.material_system.controller;

import com.example.material_system.config.ConstantConfig;
import com.example.material_system.entity.Material;
import com.example.material_system.mapper.CollegeMapper;
import com.example.material_system.service.MaterialService;
import com.example.material_system.util.CloseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @Autowired
    Executor executor;

    @Autowired(required = false)
    CollegeMapper collegeMapper;
    /**
     * 通过上传的文件的md5值来确定该视频是否存在
     * @param md5File
     * @return
     */
    @PostMapping("/checkFile")
    @ResponseBody
    public Boolean checkFile(@RequestParam(value = "md5File") String md5File) {
        return materialService.checkFile(md5File);
    }

    /**
     * 检查分片是否存在
     * @param md5File
     * @param chunk
     * @return
     */
    @PostMapping("/checkChunk")
    @ResponseBody
    public Boolean checkChunk(@RequestParam(value = "md5File") String md5File,
                              @RequestParam(value = "chunk") Integer chunk) {
        return materialService.checkChunk(md5File, chunk);
    }

    /**
     * 分片上传
     * @param file
     * @param md5File
     * @param chunk
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Boolean upload(@RequestParam(value = "file") MultipartFile file,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "chunk",required= false) Integer chunk) { //第几片，从0开始
        return materialService.upload(file, md5File, chunk);
    }

    /**
     * 合并分片
     * @param chunks
     * @param md5File
     * @param name
     * @return
     * @throws Exception
     */
    @PostMapping("/merge")
    @ResponseBody
    public Boolean merge(@RequestParam(value = "chunks",required =false) Integer chunks,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "name") String name,
                          @RequestParam(value = "collegeName") String collegeName,
                          @RequestParam(value = "material_upload_department")String material_upload_department) throws Exception {
        return materialService.merge(chunks, md5File, name, collegeName, material_upload_department);
    }

    /**
     * 获取所有文件信息
     * @param index
     * @param model
     * @return
     */
    @GetMapping(value = "/admin/selectAllMaterial")
    public String selectAllMaterial(Integer index,Model model,String material_upload_department){
        List<Material> materials = materialService.selectAllMaterial(index,material_upload_department);
        int count = materialService.count();
        count = count/ ConstantConfig.NUMBER+1;
        count = count>10?10:count;
        List<Integer> numberList = new ArrayList<>();
        for (int i=1;i<=count;i++){
            numberList.add(i);
        }
        model.addAttribute("materials",materials);
        model.addAttribute("numberList",numberList);
        model.addAttribute("material_upload_department",material_upload_department);
        return "/allFile";
    }

    /**
     * 查询自己的文件
     * @param material_upload_college
     * @param model
     * @return
     */
    @GetMapping(value = "/user/selectMaterialByCollege")
    public String selectMaterialByCollege(String material_upload_college, Model model){
        List<Material> materials = materialService.selectMaterialByCollege(material_upload_college);
        String college = material_upload_college;
        model.addAttribute("materials",materials);
        model.addAttribute("college",college);
        return "/selfFile";
    }

    /**
     * 删除自己的文件
     * @param material_upload_college
     * @param material_md5_id
     * @param model
     * @return
             */
    @GetMapping(value = "/user/deleteMaterialById")
    public String deleteMaterialById(String material_upload_college, String material_md5_id, Model model){
        materialService.deleteMaterialById(material_md5_id);
        List<Material> materials = materialService.selectMaterialByCollege(material_upload_college);
        model.addAttribute("materials",materials);
        model.addAttribute("college",material_upload_college);
        return "/selfFile";
    }

    /**
     * 管理员删除文件
     * @param material_md5_id
     * @return
     */
    @GetMapping(value = "/admin/adminDeleteMaterialById")
    public ModelAndView adminDeleteMaterialById(String material_md5_id, String material_upload_department){
        materialService.deleteMaterialById(material_md5_id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/selectAllMaterial");
        modelAndView.addObject("index",1);
        modelAndView.addObject("material_upload_department",material_upload_department);
        return modelAndView;
    }

    /**
     * 管理员下载文件
     * @param material_md5_id
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping(value = "/user/downLoadSelfFile")
    public void downLoadSelfFile(String material_md5_id) throws UnsupportedEncodingException {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse httpServletResponse = servletRequestAttributes.getResponse();
        materialService.downLoad(httpServletResponse, material_md5_id);
    }

    /**
     * 汇总报表
     * @return
     */
    @GetMapping(value = "/admin/creatExcel")
    public String creatExcel(){
        materialService.creatExcel();
        return "/adminIndex";
    }

    /**
     * 下载报表
     * @param httpServletResponse
     * @throws IOException
     */
    @ResponseBody
    @GetMapping(value = "/admin/donLoadExcel")
    public void donLoadExcel(HttpServletResponse httpServletResponse) throws IOException {
        File file = new File(ConstantConfig.EXCEL_FILE_PATH);
        if (file.exists()){
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode("anyuExcel.zip","UTF-8"));
            byte[] bytes = new byte[1024];
            OutputStream outputStream = httpServletResponse.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            File[] files = file.listFiles();
            if (files!=null&&files.length>0){
                FileInputStream fileInputStream=null;
                for (File f:files){
                    fileInputStream=new FileInputStream(f);
                    zipOutputStream.putNextEntry(new ZipEntry(f.getName()));
                    int len = 0;
                    while ((len = fileInputStream.read(bytes))!=-1){
                        zipOutputStream.write(bytes,0,len);
                    }
                    zipOutputStream.closeEntry();
                    CloseUtil.Close(fileInputStream);
                }
            }
            zipOutputStream.flush();
            zipOutputStream.close();
            outputStream.close();
        }
    }

    @ResponseBody
    @GetMapping(value = "/admin/downLoadFileByDepartment")
    public void downLoadFileByDepartment(String material_upload_department,HttpServletResponse httpServletResponse) throws IOException{
        File file = new File(ConstantConfig.FILE_PATH.concat(material_upload_department).concat("\\"));
        if (file.exists()){
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" +   java.net.URLEncoder.encode(material_upload_department.concat(".zip"),"UTF-8"));
            byte[] bytes = new byte[1024];
            OutputStream outputStream = httpServletResponse.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            File[] files = file.listFiles();
            if (files!=null&&files.length>0){
                FileInputStream fileInputStream=null;
                for (File f:files){
                    fileInputStream=new FileInputStream(f);
                    zipOutputStream.putNextEntry(new ZipEntry(f.getName()));
                    int len = 0;
                    while ((len = fileInputStream.read(bytes))!=-1){
                        zipOutputStream.write(bytes,0,len);
                    }
                    zipOutputStream.closeEntry();
                    CloseUtil.Close(fileInputStream);
                }
            }
            zipOutputStream.flush();
            zipOutputStream.close();
            outputStream.close();
        }
    }
}
