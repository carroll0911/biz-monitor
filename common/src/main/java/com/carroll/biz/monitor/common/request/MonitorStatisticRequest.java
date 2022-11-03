package com.carroll.biz.monitor.common.request;

import com.carroll.spring.rest.starter.validator.ValueIn;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2017/11/30 16:55 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class MonitorStatisticRequest extends BmBaseRequest {
    @ApiModelProperty(value = "统计维度, LEVEL:告警级别, SOURCE:告警源, NAME:告警名称")
    @ValueIn(allowValues = {"LEVEL", "SOURCE", "NAME"})
    private String type;

    @ApiModelProperty(value = "告警数据状态, NORMAL: 未清除, CLEARED: 已清除")
    @ValueIn(allowValues = {"NORMAL", "CLEARED"})
    private String status;
}
