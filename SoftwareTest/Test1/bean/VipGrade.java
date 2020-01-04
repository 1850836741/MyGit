package SoftwareTest.Test1.bean;

/**
 * Vip等级
 */
public enum VipGrade{
    F(0.00),S(0.1),G(0.25),P(0.5);
    private double value;
    VipGrade(double value){
        this.value=value;
    }

    public double getValue(){
        return this.value;
    }
}