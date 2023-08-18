package top.meethigher;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ExcelImportApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(ExcelImportApplication.class, args);
//    }

    /**
     * excel标头
     */
    private static final String[] header = new String[]{
            "车牌号",
            "车辆类型",
            "车辆情况",
            "所属单位",
            "停车场",
            "停车位编号",
            "生效日期-毫秒时间戳",
            "失效日期-毫秒时间戳"
    };

    public static void main(String[] args) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet st = wb.createSheet("数据");
        //表头样式
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        //字体样式
        Font fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short) 12);
        style.setFont(fontStyle);
        //写标题
        XSSFRow row = st.createRow(0);
        st.createFreezePane(0, 1, 0, 1);
        for (int i = 0; i < header.length; i++) {
            String value = header[i];
            XSSFCell cell = row.createCell(i);
            st.setColumnWidth(i, value.length() * 1000);
            cell.setCellStyle(style);
            cell.setCellValue(value);
        }
        String[] strs = {"语文","数学","体育"};
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(st);
        XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint)helper.createExplicitListConstraint(strs);
        //参数顺序：开始行、结束行、开始列、结束列
        CellRangeAddressList addressList = new CellRangeAddressList(1,5,2,2);
        XSSFDataValidation validation = (XSSFDataValidation)helper.createValidation(constraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        st.addValidationData(validation);
        //添加下滑栏
        wb.write(new FileOutputStream("test.xlsx"));
    }
}
