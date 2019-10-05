package SoftwareTest.Test1.Unit;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 工具类基类,规范了工具类的基本方法
 */
public interface Unit {
    void CalculationPrice(int number) throws IOException;
    List getList();
    void Refresh();
    void writeFile() throws IOException;
    void readFile() throws IOException;
    boolean fileIsexits(File file);
    void Implement() throws Exception;
}
