package com.carroll.biz.monitor.analyzer.request;

import com.carroll.spring.rest.starter.validator.ValueIn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author: hehongbo
 * @date 2019/10/12
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
@ApiModel
public class MonitorItemSaveRequest {
    @ApiModelProperty(value = "名称", required = true, position = 1)
    private String name;

    @ApiModelProperty(value = "tag", required = true, position = 2)
    private String tag;

    @ApiModelProperty(value = "级别 BUSINESS: 业务, SYSTEM: 系统", required = true, position = 2)
    @ValueIn(allowValues = {"BUSINESS", "SYSTEM"})
    private String category;

    @ApiModelProperty(value = "告警级别, MAJOR: 严重, CRITICAL: 紧急 , WARNING: 警告 , EVENT: 事件",required = true,position = 3)
    @ValueIn(allowValues = {"MAJOR", "CRITICAL", "WARNING","EVENT"})
    private String level;

    @ApiModelProperty(value = "描述", position = 4)
    private String description;

    @ApiModelProperty(value = "处理意见", position = 5)
    private String suggest;

    @ApiModelProperty(value = "所属项目id")
    @NotNull
    private String projectId;

    @ApiModelProperty(value = "判断执行结果的脚本", position = 6)
    private String resultScript;

}
