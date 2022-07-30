package top.meethigher.rest.controller.service.impl;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.meethigher.constant.ResponseEnum;
import top.meethigher.exception.WebCommonException;
import top.meethigher.rest.controller.service.CommonExcelService;
import top.meethigher.utils.ExcelUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * 公共excel操作
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 10:30
 */
@Service
public class CommonExcelServiceImpl implements CommonExcelService {
    @Override
    public void templateDown(HttpServletResponse response, String fileName, XSSFWorkbook xssfWorkbook) throws IOException {
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
        // 实现文件下载
        OutputStream os = response.getOutputStream();
        xssfWorkbook.write(os);

    }

    @Override
    public XSSFWorkbook verifyExcel(MultipartFile file, XSSFWorkbook templateXssfWorkBook) throws WebCommonException, IOException {
        String originalFilename = file.getOriginalFilename();
        if (!ExcelUtil.isValidExcel(originalFilename)) {
            throw new WebCommonException(ResponseEnum.EXCEL_NOT_SUPPORT);
        }
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        if (!ExcelUtil.isValidExcelCel(ExcelUtil.getExcelTitleCel(xssfWorkbook),
                ExcelUtil.getExcelTitleCel(templateXssfWorkBook))) {
            throw new WebCommonException(ResponseEnum.EXCEL_NOT_MATCH);
        }
        return xssfWorkbook;
    }
}
