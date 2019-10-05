package SoftwareTest.Test1.bean;

/**
 * 规则对象基类
 */
public class Section implements Comparable<Section>{
    public int Section;

    public Section(){
    }

    public Section(int Section){
        this.Section=Section;
    }
    public int getSection() {
        return Section;
    }

    @Override
    public int compareTo(SoftwareTest.Test1.bean.Section section) {
        return this.Section-section.Section;
    }
}
