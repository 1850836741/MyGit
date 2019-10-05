package SoftwareTest.Test1.bean;
/**
 * 加以扩展，适应AC会员积分结算规则
 */
public class VipSection extends SoftwareTest.Test1.bean.Section {
    //座舱级别
    public String Value;
    //座舱级别对应的百分比
    public double DSection;
    public  VipSection(int Section){
        super(Section);

    }
    public VipSection() {
        super();
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getValue() {
        return Value;
    }

    public void setDSection(double DSection) {
        this.DSection = DSection;
    }

    public double getDSection() {
        return DSection;
    }
}
