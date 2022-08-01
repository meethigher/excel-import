package top.meethigher.utils;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

/**
 * excel工具类单元测试
 *
 * @author chenchuancheng
 * @since 2022/8/1 14:47
 */
//@SpringBootTest
class ExcelUtilTest {

    @Test
    void testGetDoubleCel() {
        ExcelUtil.getDoubleCel(null);
    }

    @Test
    void testGetStringCel() {
        ExcelUtil.getDoubleCel(null);
    }

    @Test
    void testGetExcelTitleCel() throws Exception {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\14251\\Desktop\\gis服务器-新-部署K8S后.xlsx"));
        String[] excelTitleCel = ExcelUtil.getExcelTitleCel(xssfWorkbook);
        System.out.println(excelTitleCel.length);
    }

    @Test
    void testIsValidExcelCel() {
        Assertions.assertTrue(ExcelUtil.isValidExcelCel(new String[]{
                "1", "2", "3"
        }, new String[]{
                "1", "2"
        }));
    }

    @Test
    void testIsValidExcel() {
        System.out.println(ExcelUtil.isValidExcel("fa.xls"));
        System.out.println(ExcelUtil.isValidExcel("fa.xlsx"));
        System.out.println(ExcelUtil.isValidExcel("fa.xlS"));
        System.out.println(ExcelUtil.isValidExcel("fa.dioc"));
    }

    @Test
    void testInteger() {
        Integer a1 = 4;
        Integer a2 = 4;
        System.out.println(a1 == a2);//true

        Integer a3 = 128;
        Integer a4 = 128;

        System.out.println(a3 == a4);//false
    }
}
