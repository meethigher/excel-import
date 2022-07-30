package top.meethigher.exception;


import top.meethigher.constant.ResponseEnum;

/**
 * WebCommonException
 *
 * @author chenchuancheng
 * @github https://github.com/meethigher
 * @blog https://meethigher.top
 * @time 2022/3/6 11:23
 */
public class WebCommonException extends Exception {


    private final int code;
    private final String desc;

    public WebCommonException(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public WebCommonException(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.desc = responseEnum.getDesc();
    }

    public WebCommonException(ResponseEnum responseEnum, String appendMsg) {
        this.code = responseEnum.getCode();
        this.desc = String.format("%s: %s", responseEnum.getDesc(), appendMsg);
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
