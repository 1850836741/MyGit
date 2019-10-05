package SoftwareTest.Test1;

import SoftwareTest.Test1.Unit.CommodityUnit;
import java.io.IOException;

public class mainSubject {
    public static void main(String[] args) throws IOException {
        ProjectInitiation.init(new CommodityUnit());
    }
}
