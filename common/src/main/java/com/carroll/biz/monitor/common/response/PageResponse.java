package com.carroll.biz.monitor.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by tangbin on 17/6/6.
 */
@Getter
@Setter
@ApiModel("分页对象")
public class PageResponse extends MonitorBaseResponse {
    @ApiModelProperty(value = "数据总数")
    private long totalElements;
    @ApiModelProperty(value = "总页数")
    private int totalPages;
    @ApiModelProperty(value = "当前页码")
    private int number;
    @ApiModelProperty(value = "当前页数据总数")
    private int numberOfElements;
    @ApiModelProperty(value = "是否含有第一条数据")
    private boolean first;
    @ApiModelProperty(value = "是否含有最后一条数据")
    private boolean last;
    @ApiModelProperty(value = "页大小")
    private int size;

    public PageResponse() {
    }

    public PageResponse(long totalElements, int number, int size) {
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
        if (totalElements % size == 0) {
            this.totalPages = (int) (totalElements / size);
        } else {
            this.totalPages = (int) (totalElements / size + 1);
        }
        //第一页
        if (number == 0) {
            this.setFirst(true);
        }
        //最后一页
        if (number == this.totalPages - 1) {
            this.setLast(true);
        }
    }
}
