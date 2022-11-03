package com.carroll.biz.monitor.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created on 2017/11/30 17:04 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class WarningDataStatisticDto implements Serializable{
    @ApiModelProperty(value = "统计维度描述")
    private String desc;

    @ApiModelProperty(value = "次数")
    private Long times;

    @ApiModelProperty(value = "状态")
    private String status;
}
