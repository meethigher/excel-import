package top.meethigher.constant;

/**
 * 异常枚举
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 09:16
 */
public enum ResponseEnum {
    SUCCESS(0, "成功"),
    FAILURE(10001, "失败"),
    PARAMS_CHECK_ERR(10002, "参数错误"),
    DOWN_TEMPLATE_ERR(10003, "下载模板失败"),
    EXCEL_NOT_SUPPORT(10004, "不支持的excel格式"),
    EXCEL_VERIFY_FAIL(10005, "校验excel不通过"),
    EXCEL_NOT_MATCH(10006, "上传excel与模板不匹配");

    public final int code;
    public final String desc;

    ResponseEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
