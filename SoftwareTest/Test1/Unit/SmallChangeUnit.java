package SoftwareTest.Test1.Unit;

import SoftwareTest.Test1.bean.Section;
import SoftwareTest.Test1.bean.SmallChangeSection;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * 零钱结算程序测试工具类
 */
public class SmallChangeUnit implements Unit{

    List<Section> list=new ArrayList<>();                                   //存放规则
    boolean JudgeSort=false;                                               //判断是否排序过
    int start=0;                                                          //指向规则
    FileWriter fileWriter;
    FileReader fileReader;

    /**
     * 依照规则来计算最终数值
     * @param number
     * @return
     */
    @Override
    public void CalculationPrice(int number) throws IOException {
        int numberCopy=number;
        number=20-number;
        StringBuilder results=new StringBuilder();
        results.append("<");
        start=list.size()-1;
        //判断是否排序
        if(!JudgeSort){
            Collections.sort(list);
            JudgeSort=true;
        }
        //计算并将结果以字符串形式输出
        int count=0;
        while (number>0&number<=20){
            if(start>=0&number>=list.get(start).getSection()){
                number-=list.get(start).getSection();
                count++;
                if (number==0){
                    results.append("<").append(list.get(start).getSection()).append(",").append(count).append(">");
                }
            }else {
                results.append("<").append(list.get(start).getSection()).append(",").append(count).append(">");
                if(number!=0){
                    results.append(",");
                }
                count=0;
                start--;
            }
        }
        results.append(">");
        if (results.toString().equals("<>")){
            results=new StringBuilder("N/A");
        }
        System.out.println(results.toString());
        File resultFile=new File("E:/SoftHomeworkTest/SmallChangeResult.txt");
        if (!resultFile.exists()){
            resultFile.createNewFile();
        }
        FileWriter writer=new FileWriter(resultFile,true);
        writer.write("商品价格:"+numberCopy+"   "+"找零方案:"+ results.toString()+"\r\n");
        CloseUnit.Close(writer);
    }

    @Override
    public List getList() {
        return list;
    }

    @Override
    public void Refresh() {
        list.clear();
        JudgeSort=false;
        start=0;
    }

    /**
     * 写入规则文件
     * @throws IOException
     */
    @Override
    public void writeFile() throws IOException {
        fileWriter=new FileWriter("E:/SoftHomeworkTest/SmallChangeFile.txt",true);
        Section section=null;
        for(int i=0;i<list.size();i++){
            section=list.get(i);
            fileWriter.write("金额:"+section.getSection()+".\r\n");
        }
        fileWriter.flush();
        CloseUnit.Close(fileWriter);
    }

    /**
     * 读取规则文件
     * @throws IOException
     */
    @Override
    public void readFile() throws IOException {
        StringBuilder stringBuilder=new StringBuilder();
        File file=new File("E:/SoftHomeworkTest/SmallChangeFile.txt");
        if (!fileIsexits(file)){
            file.createNewFile();
            writeFile();
        }
        fileReader=new FileReader(file);
        int result=0;
        char[] buffer=new char[1024];
        //读取并添加到stringBuilder中
        while ((result=fileReader.read(buffer))!=-1){
            stringBuilder.append(buffer);
        }
        CloseUnit.Close(fileReader);
        String BuilderString = stringBuilder.toString().trim();
        System.out.println(BuilderString);
        List<String> Handle=new ArrayList<>();
        int start=0;
        int end=0;
        //解析stringBuilder
        while (true){
            end=BuilderString.indexOf(".");
            Handle.add(BuilderString.substring(start,end));
            start=end+1;
            if (start>=BuilderString.trim().length()){
                break;
            }
            BuilderString=BuilderString.substring(start);
            start=0;
        }
        //将解析出来的规则，装配到规则对象中,然后加以计算
        Section section=null;
        for (int i=0;i<Handle.size();i++){
            int S=Handle.get(i).indexOf(":")+1;
            section=new Section(Integer.parseInt(Handle.get(i).substring(S)));
            list.add(section);
        }
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
    @Override
    public void Implement() throws Exception {
        File file = new File("E:/SoftHomeworkTest/SmallChangeFile.txt");
        Scanner scanner = new Scanner(System.in);
        if (!file.exists()) {
            System.out.println("请输入规则,输入next跳出规则输入，进入参数输入模块");
            while (!scanner.hasNext("next")) {
                String value = scanner.next();
                Section smallChangeSection = toSection(value);
                list.add(smallChangeSection);
                System.out.println("请输入规则");
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
    public SmallChangeSection toSection(String value){
        int Scation=Integer.parseInt(value);
        return new SmallChangeSection(Scation);
    }
}
