package com.carroll.biz.monitor.analyzer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: hehongbo
 * @date 2019/11/19
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class WarnDataCallBackDto {

    @ApiModelProperty(value = "应用名称")
    private String projectName;
    @ApiModelProperty(value = "应用ID")
    private String projectId;
    @ApiModelProperty(value = "应用Tag")
    private String projectTag;

    @ApiModelProperty(value = "推送地址")
    private String callback;

    @ApiModelProperty(value = "启用状态")
    private boolean enable;
}
