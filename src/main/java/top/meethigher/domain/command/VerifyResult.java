package top.meethigher.domain.command;

/**
 * 校验数据所需的payload
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 16:30
 */
public class VerifyResult<T> {

    /**
     * 是否合法
     */
    private boolean isValid;

    /**
     * 不合法时的错误信息
     */
    private String errorMsg;

    /**
     * 返回合法的对象
     */
    private T command;

    public VerifyResult(boolean isValid, String errorMsg, T command) {
        this.isValid = isValid;
        this.errorMsg = errorMsg;
        this.command = command;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getCommand() {
        return command;
    }

    public void setCommand(T command) {
        this.command = command;
    }
}
