package top.meethigher.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.meethigher.constant.ResponseEnum;
import top.meethigher.domain.request.base.BaseResponse;
import top.meethigher.exception.WebCommonException;

/**
 * 全局异常捕获
 * Json返回
 *
 * @author chenchuancheng
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2022/3/6 11:30
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = WebCommonException.class)
    public BaseResponse handleException(WebCommonException e) {
        log.error("WebCommonException :{} ", e.getMessage());
        e.printStackTrace();
        return new BaseResponse<>(e.getCode(), e.getDesc());
    }

    /**
     * 数据校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        log.warn("exception :{} ", e.getMessage());
        BaseResponse response = new BaseResponse();
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        response.setCode(ResponseEnum.PARAMS_CHECK_ERR.code);
        response.setDesc(message);
        return response;
    }


    /**
     * 数据校验异常处理
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse handleException(Exception e) {
        e.printStackTrace();
        log.warn("exception :{} ", e.getMessage());
        return new BaseResponse<>(ResponseEnum.FAILURE.code,ResponseEnum.FAILURE.desc);
    }


}

