package com.carroll.biz.monitor.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 告警数据
 * Created on 202017/11/22 15:26 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class WarningDataCallBackDto extends BaseDto {
    //应用tag
    @ApiModelProperty(value = "应用tag")
    private String projectTag;
    //回调地址
    @ApiModelProperty(value = "回调地址")
    private String callback;
    //启用状态
    @ApiModelProperty(value = "启用状态")
    private boolean enable;
}
