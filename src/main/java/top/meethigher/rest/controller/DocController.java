package top.meethigher.rest.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.meethigher.domain.request.DocRequest;
import top.meethigher.domain.request.DocResponse;
import top.meethigher.domain.request.ImportDocErrorResponse;
import top.meethigher.domain.request.base.BasePageResponse;
import top.meethigher.domain.request.base.BaseResponse;
import top.meethigher.exception.WebCommonException;
import top.meethigher.rest.controller.service.DocService;
import top.meethigher.utils.ResponseUtils;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 控制器
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 09:02
 */
@RestController
@Api(tags = "档案")
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @ApiOperation(value = "模板导入", notes = "模板导入")
    @PostMapping("/excelImport")
    public BaseResponse<ImportDocErrorResponse> excelImport(@RequestParam("file") MultipartFile file) throws WebCommonException, IOException {
        return ResponseUtils.getSuccessResponse(docService.excelImport(file));
    }

    @ApiOperation(value = "模板下载", notes = "模板下载")
    @GetMapping("/downExcel")
    public void downExcel() throws WebCommonException {
        docService.downExcel();
    }

    @ApiOperation(value="档案查询",notes = "档案查询")
    @PostMapping("/docPageQuery")
    public BaseResponse<BasePageResponse<List<DocResponse>>> docPageQuery(@RequestBody @Valid DocRequest request) {
        return ResponseUtils.getSuccessResponse(docService.docPageQuery(request));
    }

}
