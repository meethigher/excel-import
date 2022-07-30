package top.meethigher.rest.controller.service;

import org.springframework.web.multipart.MultipartFile;
import top.meethigher.domain.request.DocRequest;
import top.meethigher.domain.request.DocResponse;
import top.meethigher.domain.request.ImportDocErrorResponse;
import top.meethigher.domain.request.base.BasePageResponse;
import top.meethigher.exception.WebCommonException;

import java.io.IOException;
import java.util.List;

/**
 * 档案
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 09:06
 */
public interface DocService {

    /**
     * excel导入
     *
     * @param file
     */
    ImportDocErrorResponse excelImport(MultipartFile file) throws WebCommonException, IOException;

    /**
     * 下载excel
     */
    void downExcel() throws WebCommonException;

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    BasePageResponse<List<DocResponse>> docPageQuery(DocRequest request);
}
