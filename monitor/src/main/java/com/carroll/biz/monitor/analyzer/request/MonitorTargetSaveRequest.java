package com.carroll.biz.monitor.analyzer.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 告警对象保存请求参数
 *
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@ApiModel
@Getter
@Setter
public class MonitorTargetSaveRequest {
    @ApiModelProperty("id")
    private String id;
    @NotNull
    @NotEmpty
    @ApiModelProperty("编码")
    private String code;
    @NotNull
    @NotEmpty
    @ApiModelProperty("应用ID")
    private String projectId;
    @NotEmpty
    @NotNull
    @ApiModelProperty("名称")
    private String name;
}
