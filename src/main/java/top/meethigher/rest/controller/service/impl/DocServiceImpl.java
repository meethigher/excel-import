package top.meethigher.rest.controller.service.impl;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import top.meethigher.constant.ResponseEnum;
import top.meethigher.dao.DocRepository;
import top.meethigher.domain.command.ImportDocCommand;
import top.meethigher.domain.command.VerifyResult;
import top.meethigher.domain.entity.Doc;
import top.meethigher.domain.request.DocRequest;
import top.meethigher.domain.request.DocResponse;
import top.meethigher.domain.request.ImportDocErrorResponse;
import top.meethigher.domain.request.base.BasePageResponse;
import top.meethigher.exception.WebCommonException;
import top.meethigher.rest.controller.service.CommonExcelService;
import top.meethigher.rest.controller.service.DocService;
import top.meethigher.utils.ExcelUtil;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 档案
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 09:06
 */
@Service
public class DocServiceImpl implements DocService {

    private final Logger log = LoggerFactory.getLogger(DocServiceImpl.class);

    @Resource
    private DocRepository docRepository;

    @Resource
    private CommonExcelService commonExcelService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ImportDocErrorResponse excelImport(MultipartFile file) throws WebCommonException, IOException {
        log.info("start");
        //读取excel并校验excel版本、模板匹配
        ClassPathResource resource = new ClassPathResource("template/doc.xlsx");
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resource.getInputStream());
        xssfWorkbook = commonExcelService.verifyExcel(file, xssfWorkbook);
        //校验数据格式、校验业务数据(demo测试此处不做展示)
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        ImportDocErrorResponse errorResponse = new ImportDocErrorResponse();
        List<Doc> list = new LinkedList<>();
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            XSSFRow row = sheet.getRow(i);
            String id = ExcelUtil.getStringCel(row.getCell(0));
            String name = ExcelUtil.getStringCel(row.getCell(1));
            Double locLon = ExcelUtil.getDoubleCel(row.getCell(2));
            Double locLat = ExcelUtil.getDoubleCel(row.getCell(3));
            String loc = ExcelUtil.getStringCel(row.getCell(4));
            String locId = ExcelUtil.getStringCel(row.getCell(5));
            String machineCode = ExcelUtil.getStringCel(row.getCell(6));
            String cardId = ExcelUtil.getStringCel(row.getCell(7));
            VerifyResult<ImportDocCommand> result = ImportDocCommand.verify(id, name, locLon, locLat, loc, locId,
                    machineCode, cardId);
            if (result.isValid()) {
                Doc doc = new Doc();
                ImportDocCommand command = result.getCommand();
                BeanUtils.copyProperties(command, doc);
                list.add(doc);
            } else {
                errorResponse.getError().add(String.format("第 %s 行数据出现错误: %s", i + 1, result.getErrorMsg()));
            }
        }
        //事务更新入库
        docRepository.saveAll(list);
        log.info("end--正确数据{}条--打回数据{}条", list.size(), errorResponse.getError().size());
        return errorResponse;
    }

    @Override
    public void downExcel() throws WebCommonException {
        //获取当前线程绑定的request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(attributes)) {
            return;
        }
        try {
            HttpServletResponse response = attributes.getResponse();
            ClassPathResource resource = new ClassPathResource("template/doc.xlsx");
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(resource.getInputStream());
            commonExcelService.templateDown(response, "doc.xlsx", xssfWorkbook);
        } catch (Exception e) {
            throw new WebCommonException(ResponseEnum.DOWN_TEMPLATE_ERR, e.getMessage());
        }
    }

    private Specification<Doc> getSpecification(DocRequest request) {
        return (root, cq, cb) -> {
            List<Predicate> list = new LinkedList<>();
            if (!ObjectUtils.isEmpty(request.getId())) {
                list.add(cb.like(root.get("id"), "%" + request.getId() + "%"));
            }
            if (!ObjectUtils.isEmpty(request.getLoc())) {
                list.add(cb.equal(root.get("loc"), "%" + request.getLoc() + "%"));
            }
            if (!ObjectUtils.isEmpty(request.getName())) {
                list.add(cb.like(root.get("name"), "%" + request.getName() + "%"));
            }
            cq.orderBy(cb.desc(root.get("id")));
            Predicate[] predicates = new Predicate[list.size()];
            return cb.and(list.toArray(predicates));
        };
    }

    @Override
    public BasePageResponse<List<DocResponse>> docPageQuery(DocRequest request) {
        //Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        Page<Doc> all = docRepository.findAll(getSpecification(request), pageable);
        List<DocResponse> list = new LinkedList<>();
        for (Doc x : all) {
            DocResponse y = new DocResponse();
            BeanUtils.copyProperties(x, y);
            list.add(y);
        }
        BasePageResponse<List<DocResponse>> basePageResponse = new BasePageResponse<>();
        BeanUtils.copyProperties(all, basePageResponse);
        basePageResponse.setNumber(request.getPageIndex());
        basePageResponse.setContent(list);
        return basePageResponse;


    }
}
