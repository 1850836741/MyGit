package SoftwareTest.Test1.Unit;
import SoftwareTest.Test1.bean.VipGrade;
import SoftwareTest.Test1.bean.VipSection;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 有关AC会员积分计算程序测试的工具包
 */
public class VipUnit implements Unit{
    List<VipSection> list=new ArrayList<>();                                     //存放规则
    FileWriter fileWriter;
    FileReader fileReader;
    String Cockpit;                                                              //用户传入的座舱等级
    String Grade;                                                                //用户传入的VIP等级

    /**
     * 计算用户传入的用例
     * @param number
     */
    @Override
    public void CalculationPrice(int number) throws IOException {
        double integralWithCockpit=0;
        double integralWithGrade=0;
        if (!Verification()){
            return;
        }
        //获得用户输入的座舱等级所对应的百分比
        for (int i=0;i<list.size();i++){
            if (list.get(i).getValue().contains(getCockpit().toUpperCase())){
                integralWithCockpit=list.get(i).getDSection();
                break;
            }
        }
        //获得用户输入的会员等级所对应的百分比
        for (VipGrade vip:VipGrade.values()){
            if (vip.name().equalsIgnoreCase(getGrade())){
                integralWithGrade=vip.getValue();
                break;
            }
        }
        //银行家算法四舍五入
        BigDecimal bigDecimal1=new BigDecimal(integralWithCockpit).setScale(2,RoundingMode.HALF_EVEN);
        BigDecimal bigDecimal2=new BigDecimal(integralWithGrade).setScale(2,RoundingMode.HALF_EVEN);
        BigDecimal bigDecimal3=new BigDecimal(number);
        BigDecimal result1=bigDecimal1.multiply(bigDecimal3).multiply(bigDecimal2);
        BigDecimal result2=bigDecimal1.multiply(bigDecimal3);
        BigDecimal result=result1.add(result2).setScale(0, RoundingMode.HALF_EVEN);
        System.out.print("本次旅程所获得的积分为:");
        System.out.println(result);
        File resultFile=new File("E:/SoftHomeworkTest/VipResult.txt");
        if (!resultFile.exists()){
            resultFile.createNewFile();
        }
        FileWriter writer=new FileWriter(resultFile,true);
        writer.write("会员等级"+getGrade()+"   "+"座舱等级:"+getCockpit()+"   "+"里程:"
                +number+"   "+"总积分:"+result+"\r\n");
        CloseUnit.Close(writer);
    }

    @Override
    public List getList() {
        return list;
    }

    @Override
    public void Refresh() {
        list.clear();
    }

    /**
     * 将用户定义的规则写入文件保存起来
     * @throws IOException
     */
    @Override
    public void writeFile() throws IOException {
        fileWriter=new FileWriter("E:/SoftHomeworkTest/VipFile.txt",true);
        VipSection vipSection=null;
        for(int i=0;i<list.size();i++){
            vipSection=list.get(i);
            fileWriter.write("舱位:"+vipSection.getValue()+"积分:"+vipSection.getDSection()+",\r\n");
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
        File file=new File("E:/SoftHomeworkTest/VipFile.txt");
        if (!fileIsexits(file)){
            file.createNewFile();
            writeFile();
        }
        fileReader=new FileReader(file);
        int result=0;
        char[] buf=new char[1024];
        //读取并添加到stringBuilder中
        while ((result=fileReader.read(buf))!=-1){
            stringBuilder.append(buf);
        }
        String BuilderToString=stringBuilder.toString().trim();
        System.out.println(BuilderToString);
        List<String> Handle=new ArrayList<>();
        int start=0;
        int end=0;
        //解析stringBuilder
        while (true){
            end=BuilderToString.indexOf(",");
            Handle.add(BuilderToString.substring(start,end));
            start=end+1;
            if (start>=BuilderToString.trim().length()){
                break;
            }
            BuilderToString=BuilderToString.substring(start);
            start=0;
        }
        //将解析出来的规则，装配到规则对象中,然后加以计算
        VipSection vipSection=null;
        for (int i=0;i<Handle.size();i++){
            int S=Handle.get(i).indexOf(":")+1;
            int V=Handle.get(i).lastIndexOf(":")+1;
            vipSection=new VipSection();
            vipSection.setValue(Handle.get(i).substring(S,V-2));
            vipSection.setDSection(Double.parseDouble(Handle.get(i).substring(V)));
            list.add(vipSection);
        }
    }

    @Override
    public boolean fileIsexits(File file) {
        return file.exists();
    }

    /**
     * 真正的执行方法，存在规则文件就读取并让用户输入计算参数；若不存在规则文件，则先让用户输入规则并写入文件
     * @throws IOException
     */
    @Override
    public void Implement() throws IOException{
        File file = new File("E:/SoftHomeworkTest/VipFile.txt");
        Scanner scanner = new Scanner(System.in);
        List<String> cache = new ArrayList<>();
        if (!file.exists()) {
            System.out.println("请输入规则,输入next跳出规则输入，进入参数输入模块，输入格式为Q/P/S");
            while (!scanner.hasNext("next")) {
                String value = scanner.next();
                cache.add(value);
                if (cache.size()%2==0){
                    System.out.println("请输入座椅规则:");
                }else {
                    System.out.println("请输入对应百分比积分:");
                }
                if (cache.size()==2){
                    VipSection vipSection=toSection(cache);
                    list.add(vipSection);
                    cache.clear();
                }
            }
            System.out.println("参数模块，输入exit退出");
            System.out.println("请输入会员等级:");
            writeFile();
            scanner=new Scanner(System.in);
            int change=0;
            while (!scanner.hasNext("exit")){
                if (change==1){
                    System.out.println("请输入座舱等级:");
                    String value=scanner.next();
                    setCockpit(value);
                    change++;
                    continue;
                }else  if (change==2){
                    System.out.println("请输入里程:");
                    change=0;
                    String value=scanner.next();
                    CalculationPrice(Integer.parseInt(value));
                    continue;
                }
                String value=scanner.next();
                setGrade(value);
                change++;
            }
            CloseUnit.Close(1,System.in,scanner);
        }else {
            readFile();
            System.out.println("参数模块，输入exit退出");
            System.out.println("请输入会员等级:");
            scanner=new Scanner(System.in);
            int change=0;
            while (!scanner.hasNext("exit")){
                String value=scanner.next();
                if (change==0){
                    setGrade(value);
                    change++;
                    System.out.println("请输入座舱等级:");
                    continue;
                }else if (change==1){
                    setCockpit(value);
                    change++;
                    System.out.println("请输入里程:");
                    continue;
                }else  if (change==2){
                    change=0;
                    CalculationPrice(Integer.parseInt(value));
                    System.out.println("请输入会员等级:");
                    continue;
                }
            }
            CloseUnit.Close(1,System.in,scanner);
        }
    }

    public String getCockpit() {
        return Cockpit;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public void setCockpit(String cockpit) {
        Cockpit = cockpit;
    }

    public boolean Verification(){
        if (getGrade().matches("[A-Za-z]+")){
            if (getGrade().length()>1){
                System.out.println("会员等级为非单个字符...请重新输入");
                return false;
            }else if (!getGrade().matches("F|S|G|P|f|s|g|p")){
                System.out.println("会员等级不在等级字符范围中...请重新输入");
                return false;
            }
        }else {
            System.out.println("会员等级为非字符...请重新输入");
            return false;
        }
        if (getCockpit().matches("[A-Za-z]")){
            if (getCockpit().length()>1){
                System.out.println("座舱等级为非单个字符...请重新输入");
                return false;
            }
        }else {
            System.out.println("座舱等级为非字符...请重新输入");
            return false;
        }
        return true;
    }

    /**
     * 用户写规则时用于将字符串转化为对应的规则对象
     * @param value
     * @return
     */
    public VipSection toSection(List value){
        VipSection vipSection=new VipSection();
        vipSection.setValue((String) value.get(0));
        vipSection.setDSection(Double.parseDouble((String)value.get(1)));
        return vipSection;
    }
}
