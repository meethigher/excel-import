package top.meethigher.utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel工具类
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 10:03
 */
public class ExcelUtil {


    /**
     * 获取浮点
     *
     * @param cell
     * @return
     */
    public static Double getDoubleCel(Cell cell) {
        String stringCellValue = getStringCel(cell);
        if (stringCellValue == null || stringCellValue.trim().length() <= 0) {
            return null;
        }
        try {
            return Double.valueOf(stringCellValue);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取字符串
     *
     * @param cell
     * @return
     */
    public static String getStringCel(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return null;
        }
    }


    /**
     * 获取excel标题行的所有列内容
     *
     * @param xssfWorkbook
     * @return
     */
    public static String[] getExcelTitleCel(XSSFWorkbook xssfWorkbook) {
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        if (sheet == null) {
            return new String[0];
        }
        XSSFRow row = sheet.getRow(0);
        if (row == null) {
            return new String[0];
        }
        String[] strArr = new String[row.getPhysicalNumberOfCells()];
        for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
            strArr[i] = getStringCel(row.getCell(i));
        }
        return strArr;
    }

    /**
     * 校验excel的列是否符合模板规范
     *
     * @param realCel
     * @param validCel
     * @return
     */
    public static boolean isValidExcelCel(String[] realCel, String[] validCel) {
        boolean flag = true;
        for (int i = 0; i < validCel.length; i++) {
            try {
                String valid = validCel[i];
                String real = realCel[i];
                if (!valid.equals(real)) {
                    flag = false;
                    break;
                }
            } catch (Exception e) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 校验是否为excel文件
     * .xls的所有大小写 和 .xlsx的所有大小写，都属于合法excel
     *
     * @param fileName
     * @return
     */
    public static boolean isValidExcel(String fileName) {
        if (fileName == null) {
            return false;
        }
        return fileName.matches("^.+\\.(?i)(xls)$")
                || fileName.matches("^.+\\.(?i)(xlsx)$");
    }

}
