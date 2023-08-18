package top.meethigher;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class PoiUtil {

    private static final String MAIN_SHEET = "省市区";

    private static final String SHEET_MAP = "市区";


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
            char endPrefix = 'A';
            char endSuffix = 'A';
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


    /**
     * 导出三级联动and单个下拉框的excel
     *
     * @param filePath   文件输出地址
     * @param headers    表头
     * @param mapOneList 一级所有内容
     * @param map        三级联动对应内容
     * @param map       导出数据
     */
    public static void export(String filePath, List<String> headers, List<String> mapOneList, Map<String, List<String>> map) {
    	List<String> list = new ArrayList<>();
        // 创建workbook
        HSSFWorkbook hssfWorkBook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet mainSheet = hssfWorkBook.createSheet(MAIN_SHEET);// 主sheet
        // 创建表头
        initHeaders(hssfWorkBook, mainSheet, headers);
        //导出数据到主sheet
        setMainSheet(mainSheet, list);

        //三级联动 sheet
        HSSFSheet mapSheet = hssfWorkBook.createSheet(SHEET_MAP);

        //省市区关系sheet，设置sheet是否隐藏 true:是，false:否
        hssfWorkBook.setSheetHidden(hssfWorkBook.getSheetIndex(mapSheet), true);

        // 3.写入数据 将数据写入隐藏的sheet中并做好关联关系
        writeData(hssfWorkBook, mapSheet, mapOneList, map);

        // 4.设置数据有效性
        setDataValid(hssfWorkBook, mainSheet, mapOneList, map);

        try {
        	String exisname = filePath.substring(0, filePath.lastIndexOf("/"));
            File f = new File(exisname);
            if (!f.exists()) {
                f.mkdirs();//创建目录
            }
            // 创建文件
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 数据写出文件
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
        	hssfWorkBook.write(fileOutputStream);
            System.out.println("导出成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成主页面表头
     *
     * @param wb
     * @param mainSheet
     * @param headers
     */
    private static void initHeaders(HSSFWorkbook wb, HSSFSheet mainSheet, List<String> headers) {
        //表头样式
        HSSFCellStyle style = wb.createCellStyle();
//        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //字体样式
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short) 12);
//        fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(fontStyle);
        //生成主内容
        HSSFRow rowFirst = mainSheet.createRow(0);//第一个sheet的第一行为标题
        mainSheet.createFreezePane(0, 1, 0, 1); //冻结第一行
        //写标题
        for (int i = 0; i < headers.size(); i++) {
            HSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
            mainSheet.setColumnWidth(i, 4000); //设置每列的列宽
            cell.setCellStyle(style); //加样式
            cell.setCellValue(headers.get(i)); //往单元格里写数据
        }
    }


    private static void setMainSheet(HSSFSheet mainSheet, List<String> list) {
        for (int j = 0; j < list.size(); j++) {
            //行
            Row row = mainSheet.createRow(j + 1);
            Object obj = list.get(j);
            if (obj != null) {
                Field[] fields = obj.getClass().getDeclaredFields();
                try {
                    for (int i = 0; i < fields.length; i++) {
                        if (i == 0) {
                            //excel 第一列 为序号 TODO
                            Cell cell = row.createCell(i);
                            cell.setCellValue(j + 1);
                        } else {
                            Field f = fields[i];
                            f.setAccessible(true);
                            Object value = f.get(obj);
                            if (value == null) {
                                value = "";
                            }
                            //列
                            Cell cell = row.createCell(i);
                            cell.setCellValue(value.toString());
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setDataValid(HSSFWorkbook HSSFWorkBook, HSSFSheet mainSheet, List<String> provinceList, Map<String, List<String>> siteMap) {
        //设置省份下拉
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper((HSSFSheet) mainSheet);
        String[] dataArray = provinceList.toArray(new String[0]);
        HSSFSheet hidden = HSSFWorkBook.createSheet("hidden");
        HSSFCell cell = null;
        for (int i = 0, length = dataArray.length; i < length; i++) {
            String name = dataArray[i];
            HSSFRow row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }

        // 省份隐藏sheet
        Name namedCell = HSSFWorkBook.createName();
        namedCell.setNameName("hidden");
        namedCell.setRefersToFormula("hidden!$A$1:$A$" + dataArray.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden");

        // 四个参数分别是：起始行、终止行、起始列、终止列.
        // 1 (省份下拉框代表从excel第1+1行开始) 10(省份下拉框代表从excel第1+10行结束) 1(代表第几列开始，0是第一列，1是第二列) 1(代表第几列结束，0是第一列，1是第二列)
        CellRangeAddressList provinceRangeAddressList = new CellRangeAddressList(1, 10, 1, 1);
        DataValidation provinceDataValidation = dvHelper.createValidation(constraint, provinceRangeAddressList);
        provinceDataValidation.createErrorBox("error", "请选择正确");
        provinceDataValidation.setShowErrorBox(true);
        //设置sheet是否隐藏  true:隐藏/false:显示
        HSSFWorkBook.setSheetHidden(HSSFWorkBook.getSheetIndex(hidden), true);
        mainSheet.addValidationData(provinceDataValidation);

        // 设置市、区下拉
        // i <= 10 ,10代表市、区下拉框到10+1行结束
        for (int i = 0; i <= 10; i++) {
        	// "B"是指父类所在的列，i+1初始值为1代表从第2行开始，2要与“B”对应，为B的列号加1，假如第一个参数为“C”，那么最后一个参数就3
            setDataValidation('B', mainSheet, i + 1, 2);
        }
    }

    /**
     * 设置有效性
     *
     * @param offset 主影响单元格所在列，即此单元格由哪个单元格影响联动
     * @param sheet
     * @param rowNum 行数
     * @param colNum 列数
     */
    private static void setDataValidation(char offset, HSSFSheet sheet, int rowNum, int colNum) {
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(sheet);
        DataValidation dataValidationList1;
        DataValidation dataValidationList2;
        dataValidationList1 = getDataValidationByFormula("INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum, dvHelper);
        dataValidationList2 = getDataValidationByFormula("INDIRECT($" + (char) (offset + 1) + (rowNum) + ")", rowNum, colNum + 1, dvHelper);
        sheet.addValidationData(dataValidationList1);
        sheet.addValidationData(dataValidationList2);
    }


    private static DataValidation getDataValidationByFormula(String formulaString, int naturalRowIndex, int naturalColumnIndex, HSSFDataValidationHelper dvHelper) {
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(formulaString);
        CellRangeAddressList regions = new CellRangeAddressList(naturalRowIndex, 65535, naturalColumnIndex, naturalColumnIndex);
        HSSFDataValidation data_validation_list = (HSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        if (data_validation_list instanceof HSSFDataValidation) {
            data_validation_list.setShowErrorBox(true);
        }
        // 设置输入信息提示信息
        //data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        return data_validation_list;
    }


    private static void writeData(HSSFWorkbook hssfWorkBook, HSSFSheet mapSheet, List<String> provinceList, Map<String, List<String>> siteMap) {
        //循环将父数据写入siteSheet的第1行中
        int siteRowId = 0;
        HSSFRow provinceRow = mapSheet.createRow(siteRowId++);
        provinceRow.createCell(0).setCellValue("父列表");
        for (int i = 0; i < provinceList.size(); i++) {
            provinceRow.createCell(i + 1).setCellValue(provinceList.get(i));
        }
        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        Iterator<String> keyIterator = siteMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            List<String> son = siteMap.get(key);
            HSSFRow siteRow = mapSheet.createRow(siteRowId++);
            siteRow.createCell(0).setCellValue(key);
            for (int i = 0; i < son.size(); i++) {
                siteRow.createCell(i + 1).setCellValue(son.get(i));
            }

            // 添加名称管理器
            String range = getRange(1, siteRowId, son.size());
            Name name = hssfWorkBook.createName();
            name.setNameName(key);
            String formula = mapSheet.getSheetName() + "!" + range;
            name.setRefersToFormula(formula);
        }
    }


    public static void main(String[] args) {
        //文件名称
        String filePath = "D://test.xls";
        //excel 表头
        List<String> headers = Arrays.asList("序号","省","市", "区");
        //所有一级
        List<String> mapOneList = new ArrayList<String>();
        mapOneList.add("广东省");
        mapOneList.add("湖北省");
        mapOneList.add("广西壮族自治区");
        //关系map
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        // 省市
        map.put("广东省", Arrays.asList("广州市", "佛山市"));
        map.put("湖北省", Arrays.asList("武汉市", "荆州市"));
        map.put("广西壮族自治区", Arrays.asList("南宁市", "柳州市","桂林市","玉林市","北海市","梧州市","防城港市","钦州市","贵港市","贺州市","来宾市","崇左市","河池市","百色市"));

        //市区
        map.put("广州市", Arrays.asList("白云区", "越秀区"));
        map.put("佛山市", Arrays.asList("顺德区", "南海区"));
        map.put("玉林市", Arrays.asList("玉州区", "福绵区","容县","陆川县","博白县","兴业县","北流市"));

        //需要显示在excel的信息
        PoiUtil.export(filePath,headers,mapOneList,map);
    }

}
