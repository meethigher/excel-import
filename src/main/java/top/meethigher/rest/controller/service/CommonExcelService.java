package top.meethigher.rest.controller.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import top.meethigher.exception.WebCommonException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 公共操作excel
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 10:28
 */
public interface CommonExcelService {

    /**
     * 模板下载
     *
     * @param response
     * @param fileName     文件名
     * @param xssfWorkbook excel对象
     */
    void templateDown(HttpServletResponse response, String fileName, XSSFWorkbook xssfWorkbook) throws WebCommonException, IOException;


    /**
     * 与现有的{templateXssfWorkBook}校验excel是否合法
     *
     * @param file
     * @param templateXssfWorkBook
     * @throws WebCommonException
     */
    XSSFWorkbook verifyExcel(MultipartFile file, XSSFWorkbook templateXssfWorkBook) throws WebCommonException, IOException;
}
