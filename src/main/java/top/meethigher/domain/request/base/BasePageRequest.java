package top.meethigher.domain.request.base;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分页
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 21:42
 */
public class BasePageRequest {
    @ApiModelProperty(value = "当前页码，初始页码为 1", example = "1")
    protected Integer pageIndex = 1;

    @ApiModelProperty(value = "每页查询数量，默认为 20", example = "20")
    protected Integer pageSize = 20;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
