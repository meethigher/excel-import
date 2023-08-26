package top.meethigher;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.meethigher.utils.ExcelValidationUtils.*;

/**
 * 测试与验证导出excel
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/08/20 23:42
 */
public class TestExportExcelWithValidation {


    private final static String[] headers = new String[]{
            "性别",
            "省",
            "市",
            "区",
    };


    private static Map<String, List<String>> 省级() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("湖北省", Arrays.asList("武汉市", "襄阳市"));
        map.put("吉林省", Arrays.asList("长春市", "吉林市"));
        return map;
    }

    private static Map<String, List<String>> 市级() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("武汉市", Arrays.asList("洪山区", "江夏区"));
        map.put("长春市", Arrays.asList("宽城区", "南关区"));
        return map;
    }

    public static void main(String[] args) throws Exception {
        XSSFWorkbook wb = createOneXLSX();
        XSSFSheet st = addOneSheet(wb, "data", headers);
        addSimpleDropDownListValidation(st, new String[]{"男", "女"}, 0, 0);
        addLinkageDataValidation(wb, st, 省级(), 1, 2, "B");
        addLinkageDataValidation(wb, st, 市级(), 2, 3, "C");


        wb.write(new FileOutputStream("aaa.xlsx"));
    }
}
