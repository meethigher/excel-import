package top.meethigher.utils;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * excel工具类
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 10:03
 */
public class ExcelUtil {

    /**
     * 支持的文件类型
     */
    public enum FileType {
        /**
         * name表示文件格式
         * fileCode文件格式对应的编号
         */
        XLS_2003("excel2003", "D0CF11E0"),
        XLSX_2007("excel2007", "504B0304");

        private final String name;

        private final String fileCode;


        FileType(String name, String fileCode) {
            this.name = name;
            this.fileCode = fileCode;
        }

        public String getName() {
            return name;
        }

        public String getFileCode() {
            return fileCode;
        }

        public static FileType getFileType(String fileCode) {
            for (FileType x : FileType.values()) {
                if (x.getFileCode().equals(fileCode)) {
                    return x;
                }
            }
            return null;
        }
    }


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

    /**
     * 校验文件类型
     *
     * @param file File
     * @return 文件类型
     */
    public static FileType isValidExcelType(MultipartFile file) {
        String fileCode = getFileCode(file);
        return FileType.getFileType(fileCode);
    }


    /**
     * 获取文件头
     * 前4个字节标识文件类型
     *
     * @param file 文件
     * @return 前4个字节转换的十六进制字符串
     */
    private static String getFileCode(MultipartFile file) {
        InputStream is = null;
        String value = null;
        try {
            is = file.getInputStream();
            byte[] b = new byte[4];
            int read = is.read(b, 0, b.length);
            if (read != -1) {
                value = bytesToHexString(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }


    /**
     * 字节数组转换为十六进制
     *
     * @param bytes 字节数组
     * @return 字节数组转换后的十六进制
     */
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String s;
        for (byte b : bytes) {
            //十六进制0xff，即二进制11111111，即十进制255
            //计算机存储二进制是使用补码处理，正数的补码相同。b&0xff针对负数，做补码一致性，对正数不影响。
            //直白点说，就是byte范围是[-128,127]，通过b&0xff转换为范围为[0,255]的int
            s = Integer.toHexString(b & 0xFF).toUpperCase();
            if (s.length() < 2) {
                sb.append(0);
            }
            sb.append(s);
        }
        return sb.toString();
    }

}
