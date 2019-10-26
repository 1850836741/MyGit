package core.Unit;

import core.Beans.ConstantPoolStructure;
import core.Beans.MethodBean;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字节码操作工具包
 */
public class ClassUnit {
    //存储常量项结构总表的集合
    private static final Map<Integer, ConstantPoolStructure> map;
    //存储常量池中常量
    private static final Map<Integer,String> poolMap;
    static {
        map = new HashMap<>();
        poolMap = new HashMap<>();
        init();
    }
    /*Class文件中常量池的起始偏移*/
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;

    private static final int u1=1;
    private static final int u2=2;
    private static final int u4=4;
    private static final int u8=8;

    /*解析字节码，获取相应方法的形参名字*/
    public static List<MethodBean> GetMethodParameters(String classURL) throws FileNotFoundException {
        int findIndex = CONSTANT_POOL_COUNT_INDEX+u2;
        int keepIndex = CONSTANT_POOL_COUNT_INDEX+u2;
        int poolEnd = 0;
        int utf8Count = 0;
        String urlPrefix = ClassUnit.class.getResource("").toString();
        urlPrefix = urlPrefix.substring(urlPrefix.indexOf("/")+1);
        urlPrefix = urlPrefix.substring(0,urlPrefix.indexOf("core"));
        List<MethodBean> methodBeanList = new ArrayList<>();
        InputStream inputStream= new FileInputStream(new File(urlPrefix.concat(classURL)));
        try {
            byte[] classBytes=new byte[inputStream.available()];
            inputStream.read(classBytes);
            //获取常量池的常量个数(0处的常量特殊未定义)
            poolEnd = ByteUnits.BytesToInt(classBytes,CONSTANT_POOL_COUNT_INDEX,u2) - 1;
            ConstantPoolStructure constantPoolStructure=null;
            for (int i = 0;i < poolEnd;i++){
                constantPoolStructure=map.get(ByteUnits.BytesToInt(classBytes,findIndex,u1));
                findIndex += u1;
                if (constantPoolStructure.getIsFix()){
                    ++utf8Count;
                    findIndex += constantPoolStructure.getLength();
                }else {
                    int utf8Length = ByteUnits.BytesToInt(classBytes,findIndex,constantPoolStructure.getLength());
                    findIndex +=  constantPoolStructure.getLength();
                    poolMap.put(++utf8Count, ByteUnits.BytesToString(classBytes,findIndex,utf8Length));
                    findIndex += utf8Length;
                }
            }
            //常量池遍历完成，字段表遍历
            //跳过类的标志位
            findIndex +=u2;
            //跳过类索引和父索引，各占两个字节
            findIndex += u4;
            //跳过接口索引
            findIndex += ByteUnits.BytesToInt(classBytes,findIndex,u2) == 0 ? u2 : (ByteUnits.BytesToInt(classBytes,findIndex,u2)+1)*u2;


            //字段数量
            int fieldCount = ByteUnits.BytesToInt(classBytes,findIndex,u2);
            findIndex += u2;
            //遍历字段信息
            for (int i = 0; i < fieldCount; i++){
                keepIndex = findIndex +u2;
                findIndex += u2*3;
                int attributeCount = 0;
                if ((attributeCount = ByteUnits.BytesToInt(classBytes,findIndex,u2))> 0){
                    findIndex += u2;
                    //开始解析属性表,并直接跳过
                    for (int j=0;j<attributeCount;j++){
                        findIndex += u2;
                        findIndex += ByteUnits.BytesToInt(classBytes,findIndex,u4) + u4;
                    }
                }else {
                    findIndex += u2;
                }
            }

            //方法数量
            int methodCount = ByteUnits.BytesToInt(classBytes,findIndex,u2);
            findIndex += u2;
            for (int i = 0; i < methodCount; i++){
                keepIndex = findIndex+u2;
                findIndex += u2*3;
                int attributeCount = 0;
                if ((attributeCount = ByteUnits.BytesToInt(classBytes,findIndex,u2))>0){
                    findIndex += u2;
                    if (poolMap.get(ByteUnits.BytesToInt(classBytes,keepIndex,u2)).equals("<init>") &&
                            poolMap.get(ByteUnits.BytesToInt(classBytes,findIndex,u2)).equals("Code")){
                        //跳过名字下标，属性长度
                        findIndex += u2+u4;
                        //跳过最大栈深度和局部变量表存储空间
                        findIndex += u2+u2;
                        //跳过code_length和code字节流
                        findIndex += ByteUnits.BytesToInt(classBytes,findIndex,u4)+u4;
                        //跳过异常表
                        findIndex += u2 + ByteUnits.BytesToInt(classBytes,findIndex,u2);
                        //解析属性集合
                        int attributes_count = ByteUnits.BytesToInt(classBytes,findIndex,u2);
                        findIndex += u2;
                        for (int j = 0; j < attributes_count; j++){
                            if (!poolMap.get(ByteUnits.BytesToInt(classBytes,findIndex,u2)).equals("LocalVariableTable")){
                                findIndex += u2;
                                findIndex += ByteUnits.BytesToInt(classBytes,findIndex,u4) + u4;
                            }else {
                                findIndex +=u2;
                                int LocalVariableTableLength = ByteUnits.BytesToInt(classBytes,findIndex,u4);
                                findIndex += u4;
                                int local_variable_table_length= ByteUnits.BytesToInt(classBytes,findIndex,u2);
                                findIndex += u2;
                                MethodBean methodBean = new MethodBean();
                                for (int z = 0; z < local_variable_table_length; z++){
                                    //跳过start_pc和length
                                    findIndex += u2+u2;
                                    if (poolMap.get(ByteUnits.BytesToInt(classBytes,findIndex,u2)).equals("this")){
                                        findIndex += u2 * 3;
                                    }else {
                                        methodBean.add(poolMap.get(ByteUnits.BytesToInt(classBytes,findIndex,u2)));
                                        findIndex += u2 * 3;
                                    }
                                }
                                methodBeanList.add(methodBean);
                            }
                        }
                    }else {
                        for (int j = 0; j < attributeCount; j++){
                            findIndex += u2;
                            findIndex += ByteUnits.BytesToInt(classBytes,findIndex,u4) + u4;
                        }
                    }
                }else {
                    findIndex += u2;
                }
            }
            return methodBeanList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Integer, String> getUtf8Map(){
        return poolMap;
    }

    /*依次装入常量池中14种常量项的结构的封装对象*/
    public static void init(){
        //CONSTANT_Utf8_info
        ConstantPoolStructure constantPoolStructure = new ConstantPoolStructure(1,u2);
        constantPoolStructure.setFix(false);
        map.put(1,constantPoolStructure);

        //CONSTANT_Integer_info
        constantPoolStructure = new ConstantPoolStructure(3,u4);
        map.put(3,constantPoolStructure);

        //CONSTANT_Float_info
        constantPoolStructure = new ConstantPoolStructure(4,u4);
        map.put(4,constantPoolStructure);

        //CONSTANT_Long_info
        constantPoolStructure = new ConstantPoolStructure(5,u8);
        map.put(5,constantPoolStructure);

        //CONSTANT_Double_info
        constantPoolStructure = new ConstantPoolStructure(6,u8);
        map.put(6,constantPoolStructure);

        //CONSTANT_Class_info
        constantPoolStructure = new ConstantPoolStructure(7,u2);
        map.put(7,constantPoolStructure);

        //CONSTANT_String_info
        constantPoolStructure = new ConstantPoolStructure(8,u2);
        map.put(8,constantPoolStructure);

        //CONSTANT_Fieldref_info
        constantPoolStructure = new ConstantPoolStructure(9,u2+u2);
        map.put(9,constantPoolStructure);

        //CONSTANT_Methodref_info
        constantPoolStructure = new ConstantPoolStructure(10,u2+u2);
        map.put(10,constantPoolStructure);

        //CONSTANT_Interface-Methodref_info
        constantPoolStructure = new ConstantPoolStructure(11,u2+u2);
        map.put(11,constantPoolStructure);

        //CONSTANT_Name-AndType_info
        constantPoolStructure = new ConstantPoolStructure(12,u2+u2);
        map.put(12,constantPoolStructure);

        //CONSTANT_Method-Handle_info
        constantPoolStructure = new ConstantPoolStructure(15,u1+u2);
        map.put(15,constantPoolStructure);

        //CONSTANT_Method-Type_info
        constantPoolStructure = new ConstantPoolStructure(16,u2);
        map.put(16,constantPoolStructure);

        //CONSTANT_Invoke-Dynamic_info
        constantPoolStructure = new ConstantPoolStructure(18,u2+u2);
        map.put(18,constantPoolStructure);
    }
}
