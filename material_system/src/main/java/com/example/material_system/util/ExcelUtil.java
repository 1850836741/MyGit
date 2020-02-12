package com.example.material_system.util;

import com.example.material_system.config.ConstantConfig;
import com.example.material_system.entity.Material;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    /**
     * 创建excel表
     * @param collegeMaterialMap
     * @param excelTitle
     */
    public static void creatExcel(Map<String, List<Material>> collegeMaterialMap, String[] excelTitle){
        for (String key : collegeMaterialMap.keySet()){
            // 第一步，创建一个webbook，对应一个Excel文件
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
            // 默认列宽
            sheet.setDefaultColumnWidth(20);
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow hssfRow = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
            // 创建一个居中格式
            hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);
            // 添加excel title
            HSSFCell cell = null;
            for (int i=0;i<excelTitle.length;i++){
                cell = hssfRow.createCell(i);
                cell.setCellValue(excelTitle[i]);
                cell.setCellStyle(hssfCellStyle);
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Material> materials = collegeMaterialMap.get(key);

            for (int i=0;i<materials.size();i++){
                HSSFRow row = sheet.createRow(i+1);
                Material material = materials.get(i);
                row.createCell(0).setCellValue(i+1);

                String realName = material.getMaterial_name().substring(0,material.getMaterial_name().indexOf("-"));
                realName = realName.concat(material.getMaterial_name().substring(material.getMaterial_name().lastIndexOf(".")));
                row.createCell(1).setCellValue(realName);
                row.createCell(2).setCellValue(material.getMaterial_upload_college());
                row.createCell(3).setCellValue(material.getMaterial_upload_department());
                row.createCell(4).setCellValue(simpleDateFormat.format(material.getUpload_time()));
            }
            FileOutputStream fileOutputStream = null;
            try {
                File file = new File(ConstantConfig.EXCEL_FILE_PATH);
                if (!file.exists()){
                    file.mkdirs();
                }
                fileOutputStream = new FileOutputStream(ConstantConfig.EXCEL_FILE_PATH.concat(key).concat(".xls"));
                hssfWorkbook.write(fileOutputStream);
                CloseUtil.Close(1,hssfWorkbook,fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
