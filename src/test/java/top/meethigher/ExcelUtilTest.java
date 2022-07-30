package top.meethigher;


import org.junit.jupiter.api.Test;
import top.meethigher.utils.ExcelUtil;

/**
 * excel工具类单元测试
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 10:25
 */
//@SpringBootTest
public class ExcelUtilTest {
    @Test
    void testIsValidExcel() {
        System.out.println(ExcelUtil.isValidExcel("fa.xls"));
        System.out.println(ExcelUtil.isValidExcel("fa.xlsx"));
        System.out.println(ExcelUtil.isValidExcel("fa.xlS"));
        System.out.println(ExcelUtil.isValidExcel("fa.dioc"));
    }

    @Test
    void name() {
        Integer a1=4;

        Integer a2=4;
        System.out.println(a1==a2);//true

        Integer a3=128;
        Integer a4=128;

        System.out.println(a3==a4);//false
    }
}
