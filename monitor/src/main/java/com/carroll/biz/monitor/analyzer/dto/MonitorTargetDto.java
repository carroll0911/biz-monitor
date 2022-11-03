package com.carroll.biz.monitor.analyzer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: hehongbo
 * @date 2020/4/15
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
@ApiModel
public class MonitorTargetDto {
    @ApiModelProperty("ID")
    private String id;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("应用ID")
    private String projectId;
    @ApiModelProperty("应用名称")
    private String projectName;
}
