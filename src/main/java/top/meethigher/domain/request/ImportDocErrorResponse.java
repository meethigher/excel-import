package top.meethigher.domain.request;

import java.util.LinkedList;
import java.util.List;

/**
 * 上传Doc中存在的错误数据
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 16:24
 */
public class ImportDocErrorResponse {

    private Integer success;

    private Integer failure;

    /**
     * 错误集合。
     */
    private List<String> error=new LinkedList<>();

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }
}
