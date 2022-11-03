package com.carroll.biz.monitor.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2018/1/16 15:25 By lanli
 * <p>
 * Copyright @ 2018 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class LogDataDto {
    @ApiModelProperty(value = "监控点调用参数")
    private String params;
    @ApiModelProperty(value = "监控点调用结果")
    private String response;
    /**
     * 调用链ID
     */
    @ApiModelProperty(value = "调用链ID")
    private String traceId;
}
