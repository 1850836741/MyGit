package core.Beans;

public class ConstantPoolStructure {
    private int tag;
    private int length;
    private boolean isFix;

    public ConstantPoolStructure(int tag, int length){
        this.tag = tag;
        this.length = length;
        isFix = true;
    }

    public void setFix(boolean fix) {
        isFix = fix;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public int getTag() {
        return tag;
    }

    public boolean getIsFix() {
        return isFix;
    }
}
