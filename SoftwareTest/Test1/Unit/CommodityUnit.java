package SoftwareTest.Test1.Unit;

import SoftwareTest.Test1.bean.Section;
import SoftwareTest.Test1.bean.SectionAndValue;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * 商品结算程序测试工具类
 */
public class CommodityUnit implements Unit{

    private List<SectionAndValue> list=new ArrayList<>();                          //用于存放规则对象
    private boolean JudgeSort=false;                                               //判断是否排序过
    private int start=0;                                                           //定位number所属区间
    private FileWriter fileWriter;
    private FileReader fileReader;

    /**
     * 依照规则来计算最终数值
     * @param number
     * @return
     */
    public void CalculationPrice(int number) throws IOException {
        if (number<1||number>100){
            System.out.println("提示”请输入1~100之间的整数”");
            return;
        }
        //判断是否需要排序
        if (!JudgeSort){
            Collections.sort(list);
            JudgeSort=true;
        }
        //找到规则区间
        while (list.get(start).getSection()<=number&&start<list.size()-1){
            start++;
        }
        if (start==0){
            System.out.println(list.get(start).getValue()*number);
            return;
        }
        //value值指向
        int money=start;
       SectionAndValue sectionAndValue=list.get(--start);
        int result=(number-sectionAndValue.getSection())*list.get(money).getValue();
        while (start>0){
            start--;
            money--;
            sectionAndValue=list.get(start);
            result+=(list.get(start+1).getSection()-sectionAndValue.getSection())*list.get(money).getValue();
        }
        result+=list.get(0).getValue()*list.get(0).getSection();
        System.out.print("计算结果为:");
        System.out.println(result);
        File resultFile=new File("E:/SoftHomeworkTest/CommodityResult.txt");
        if (!resultFile.exists()){
            resultFile.createNewFile();
        }
        FileWriter writer=new FileWriter(resultFile,true);
        writer.write("购买数量:"+number+"   "+"总价:"+result+"\r\n");
        CloseUnit.Close(writer);
    }

    @Override
    public List getList() {
        return list;
    }

    public void Refresh(){
        list.clear();
        JudgeSort=false;
        start=0;
    }

    /**
     * 读取规则文件
     * @throws IOException
     */
    public void readFile() throws IOException {
        StringBuilder stringBuilder=new StringBuilder();
        File file=new File("E:/SoftHomeworkTest/CommodityFile.txt");
        char[] buf=new char[1024];
        if (!fileIsexits(file)){
            file.createNewFile();
            writeFile();
        }
        fileReader=new FileReader(file);
        int result=0;
        //读取并添加到stringBuilder中
        while ((result=fileReader.read(buf))!=-1){
            stringBuilder.append(String.valueOf(buf));
        }
        CloseUnit.Close(fileReader);
        String BuilderToString=stringBuilder.toString().trim();
        System.out.println(BuilderToString);
        List<String> Handle=new ArrayList<>();
        int start=0;
        int end=0;
        //解析stringBuilder
        while (true){
            end=BuilderToString.indexOf(".");
            Handle.add(BuilderToString.substring(start,end));
            start=end+1;
            if (start>=BuilderToString.trim().length()){
                break;
            }
            BuilderToString=BuilderToString.substring(start);
            start=0;
        }
        //将解析出来的规则，装配到规则对象中,然后加以计算
        SectionAndValue sectionAndValue=null;
        for (int i=0;i<Handle.size();i++){
            int S=Handle.get(i).indexOf(":")+1;
            int V=Handle.get(i).lastIndexOf(":")+1;
            sectionAndValue=new SectionAndValue(Integer.parseInt(Handle.get(i).substring(S,V-2)),
                    Integer.parseInt(Handle.get(i).substring(V)));
            list.add(sectionAndValue);
        }
    }

    /**
     * 写入规则文件
     * @throws IOException
     */
    public void writeFile() throws IOException {
        fileWriter=new FileWriter("E:/SoftHomeworkTest/CommodityFile.txt",true);
        SectionAndValue sectionAndValue=null;
        for(int i=0;i<list.size();i++){
            sectionAndValue=list.get(i);
            fileWriter.write("区间:"+sectionAndValue.getSection()+"值:"+sectionAndValue.getValue()+".\r\n");
        }
        fileWriter.flush();
        CloseUnit.Close(fileWriter);
    }

    /**
     * 判断文件是否存在
     * @param file
     * @return
     */
    public boolean fileIsexits(File file){
        return file.exists();
    }


    /**
     * 真正的执行方法，存在规则文件就读取并让用户输入计算参数；若不存在规则文件，则先让用户输入规则并写入文件
     * @throws IOException
     */
    public void Implement() throws IOException {
        File file = new File("E:/SoftHomeworkTest/CommodityFile.txt");
        Scanner scanner = new Scanner(System.in);
        if (!file.exists()) {
            List<String> cache = new ArrayList<>();
            System.out.println("请输入规则,输入next跳出规则输入，进入参数输入模块");
            while (!scanner.hasNext("next")) {
                String value = scanner.next();
                cache.add(value);
                if (cache.size() % 2 == 0) {
                    System.out.println("请输入规则区间");
                } else {
                    System.out.println("请输入规则相对应的值");
                }
                if (cache.size() == 2) {
                    Section sectionAndValue = toSection(cache);
                    list.add((SectionAndValue) sectionAndValue);
                    cache.clear();
                }
            }
            writeFile();
            scanner = new Scanner(System.in);
            System.out.println("参数模块，输入exit退出");
            while (!scanner.hasNext("exit")) {
                String value = scanner.next();
                CalculationPrice(Integer.valueOf(value));
            }
            CloseUnit.Close(1,System.in,scanner);
        } else {
            readFile();
            scanner = new Scanner(System.in);
            System.out.println("参数模块，输入exit退出");
            while (!scanner.hasNext("exit")) {
                String value = scanner.next();
                CalculationPrice(Integer.valueOf(value));
            }
            CloseUnit.Close(1,System.in,scanner);
        }
    }

    /**
     * 用户写规则时用于将字符串转化为对应的规则对象
     * @param value
     * @return
     */
    public SectionAndValue toSection(List value){
        int Section=Integer.parseInt((String) value.get(0));
        int Value=Integer.parseInt((String) value.get(1));
        return new SectionAndValue(Section,Value);
    }
}
