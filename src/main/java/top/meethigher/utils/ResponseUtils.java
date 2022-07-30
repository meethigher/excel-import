package top.meethigher.utils;


import top.meethigher.constant.ResponseEnum;
import top.meethigher.domain.request.base.BaseResponse;

/**
 * ResponseUtils
 *
 * @author chenchuancheng
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2022/3/6 11:25
 */
public class ResponseUtils {


    public static BaseResponse getSuccessResponse() {
        return new BaseResponse(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc());
    }

    public static <T> BaseResponse<T> getSuccessResponse(T data) {
        return new BaseResponse<T>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc(), data);
    }

    public static BaseResponse getErrorResponse(Integer code, String message) {
        return new BaseResponse(code, message);
    }
}
