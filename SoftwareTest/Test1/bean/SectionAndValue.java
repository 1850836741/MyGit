package SoftwareTest.Test1.bean;

/**
 * 加以扩展，适应商品结算规则
 */
public class SectionAndValue extends Section{
    public int Section;
    public int Value;
    public SectionAndValue(int Section,int Value){
        super(Section);
        this.Value=Value;
    }


    public int getValue() {
        return Value;
    }


    @Override
    public String toString() {
        return String.valueOf(Section)+String.valueOf(Value);
    }


    public int compareTo(SectionAndValue section) {
        if(this.getSection()==section.getSection()){
            return section.getValue()-this.getValue();
        }
        return this.Section-section.Section;
    }
}
