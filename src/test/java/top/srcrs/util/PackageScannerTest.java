package top.srcrs.util;

import org.junit.jupiter.api.Test;
import top.srcrs.Task;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试扫描
 *
 * @author : Ali
 * @date : 2020/12/5
 */
class PackageScannerTest {

    @Test
    void taskTest() {
        PackageScanner pack = new PackageScanner() {
            @Override
            public void dealClass(String className) {
                try{
                    Class<?> clazz = Class.forName(className);
                    // 判断是否实现了接口Task
                    assertTrue(Arrays.stream(clazz.getInterfaces()).parallel().anyMatch(taskI -> taskI.equals(Task.class)));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        pack.scannerPackage("top.srcrs.task");
    }

}
