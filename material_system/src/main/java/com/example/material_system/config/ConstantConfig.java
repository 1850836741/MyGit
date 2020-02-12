package com.example.material_system.config;

public class ConstantConfig {

    /*服务器存放分片的路径*/
    public final static String TEMPORARY_PATH = "F:/TemporaryFile/";

    /*服务器存放文件的路径*/
    public final static String FILE_PATH = System.getProperty("user.dir").concat("\\target\\classes\\static\\file\\");

    /*生成Excel报表的位置*/
    public final static String EXCEL_FILE_PATH = System.getProperty("user.dir").concat("\\target\\classes\\static\\excel\\");

    /*Excel报表标题*/
    public final static String[] EXCEL_TITLE = new String[]{"编号","文件名","文件所属学院","文件所属部门","上传时间"};

    /*分页时每页的个数*/
    public final static int NUMBER = 20;
}
