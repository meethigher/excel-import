package top.meethigher.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * excel验证工具类
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2023/08/20 23:55
 */
public class ExcelValidationUtils {


    private static final int minRow = 1;

    private static final int maxRow = 100;

    private static final boolean debugHideSheet = false;


    /**
     * 创建一个xlsx
     *
     * @return {@link XSSFWorkbook}
     */
    public static XSSFWorkbook createOneXLSX() {
        return new XSSFWorkbook();
    }

    /**
     * 为xlsx添加一个sheet
     *
     * @param wb        xlsx
     * @param sheetName sheet名
     * @param headers   首行标题头
     * @return sheet
     */
    public static XSSFSheet addOneSheet(XSSFWorkbook wb, String sheetName, String[] headers) {
        XSSFSheet st = wb.createSheet(sheetName);
        //表头样式
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        //字体样式
        Font fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short) 12);
        style.setFont(fontStyle);
        //单元格格式为文本
        XSSFDataFormat format = wb.createDataFormat();
        style.setDataFormat(format.getFormat("@"));
        //写标题
        XSSFRow row = st.createRow(0);
        st.createFreezePane(0, 1, 0, 1);
        for (int i = 0; i < headers.length; i++) {
            String value = headers[i];
            XSSFCell cell = row.createCell(i);
            st.setColumnWidth(i, value.length() * 1000);
            cell.setCellStyle(style);
            st.setDefaultColumnStyle(i, style);
            cell.setCellValue(value);
        }
        return st;
    }


    /**
     * 添加两层级联数据
     *
     * @param wb                  xlsx
     * @param targetSheet         目标sheet
     * @param linkageData         两层级联数据
     * @param parentCol           父列
     * @param childCol            孩子列
     * @param parentColIdentifier 父列标识符
     * @return {@link XSSFSheet}
     */
    public static XSSFSheet addLinkageDataValidation(XSSFWorkbook wb, XSSFSheet targetSheet, Map<String, List<String>> linkageData,
                                                     int parentCol, int childCol, String parentColIdentifier) {
        XSSFSheet hideSt = wb.createSheet();
        wb.setSheetHidden(wb.getSheetIndex(hideSt), !debugHideSheet);
        int rowId = 0;
        Set<String> keySet = linkageData.keySet();
        for (String parent : keySet) {
            List<String> sonList = linkageData.get(parent);
            XSSFRow row = hideSt.createRow(rowId++);
            row.createCell(0).setCellValue(parent);
            for (int i = 0; i < sonList.size(); i++) {
                XSSFCell cell = row.createCell(i + 1);
                cell.setCellValue(sonList.get(i));
            }
            // 添加名称管理器,1表示b列,从b列开始往后，都是子级
            String range = getRange(1, rowId, sonList.size());
            Name name = wb.createName();
            name.setNameName(parent);
            String formula = hideSt.getSheetName() + "!" + range;
            name.setRefersToFormula(formula);
        }
        //创建表达式校验
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(targetSheet);

//        //父级校验，如需生成更多，用户手动拖拽下拉即可。此操作会导致数组内容总长度超过255时报错
//        DataValidation parentValidation = helper.createValidation(helper.createExplicitListConstraint(keySet.toArray(new String[0])),
//                new CellRangeAddressList(minRow, maxRow, parentCol, parentCol));
//        parentValidation.createErrorBox("错误", "请选择正确的父级类型");
//        parentValidation.setShowErrorBox(true);
//        parentValidation.setSuppressDropDownArrow(true);
//        targetSheet.addValidationData(parentValidation);

        //解决长度为255的问题
        Name name = wb.createName();
        name.setNameName(hideSt.getSheetName());
        name.setRefersToFormula(hideSt.getSheetName() + "!$A$1:$A$" + keySet.size());
        DataValidation parentValidation = helper.createValidation(helper.createFormulaListConstraint(hideSt.getSheetName()), new CellRangeAddressList(minRow, maxRow, parentCol, parentCol));
        parentValidation.createErrorBox("错误", "请选择正确的父级类型");
        parentValidation.setShowErrorBox(true);
        targetSheet.addValidationData(parentValidation);

        //子级校验，如需生成更多，用户手动拖拽下拉即可
        for (int i = minRow; i < maxRow; i++) {
            DataValidation childValidation = helper.createValidation(helper.createFormulaListConstraint("INDIRECT(" + parentColIdentifier + "" + (i + 1) + ")"),
                    new CellRangeAddressList(i, i, childCol, childCol));
            childValidation.createErrorBox("错误", "请选择正确的子级类型");
            childValidation.setShowErrorBox(true);
            childValidation.setSuppressDropDownArrow(true);
            targetSheet.addValidationData(childValidation);
        }

        return hideSt;
    }

    /**
     * 添加简单下拉列表验证-下拉列表总内容不超过255字符
     *
     * @param st           sheet
     * @param dropDownList 下拉列表数据
     * @param firstCol     开始列，从0开始
     * @param lastCol      结束列，从0开始
     */
    public static void addSimpleDropDownListValidation(XSSFSheet st, String[] dropDownList, int firstCol, int lastCol) {
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(st);
        XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) helper.createExplicitListConstraint(dropDownList);
        CellRangeAddressList addressList = new CellRangeAddressList(minRow, maxRow, firstCol, lastCol);
        XSSFDataValidation validation = (XSSFDataValidation) helper.createValidation(constraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        st.addValidationData(validation);
    }


    /**
     * 添加复杂下拉列表验证-下拉列表总内容允许超过255字符
     *
     * @param wb           xlsx
     * @param dropDownList 下拉列表数据
     * @param firstCol     开始列，从0开始
     * @param lastCol      结束列，从0开始
     */
    public static void addComplexDropDownListValidation(XSSFWorkbook wb, XSSFSheet st, String[] dropDownList, int firstCol, int lastCol) {
        XSSFSheet hideSt = wb.createSheet();
        wb.setSheetHidden(wb.getSheetIndex(hideSt), !debugHideSheet);
        XSSFDataValidationHelper helper = new XSSFDataValidationHelper(st);
        for (int i = 0, length = dropDownList.length; i < length; i++) {
            String value = dropDownList[i];
            XSSFRow row = hideSt.createRow(i);
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(value);
        }
        //解决长度为255的问题
        Name name = wb.createName();
        name.setNameName(hideSt.getSheetName());
        name.setRefersToFormula(hideSt.getSheetName() + "!$A$1:$A$" + dropDownList.length);
        DataValidation parentValidation = helper.createValidation(helper.createFormulaListConstraint(hideSt.getSheetName()), new CellRangeAddressList(minRow, maxRow, firstCol, lastCol));
        parentValidation.createErrorBox("错误", "请选择正确的类型");
        parentValidation.setShowErrorBox(true);
        st.addValidationData(parentValidation);
    }


    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    private static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A', endSuffix;
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }
}
