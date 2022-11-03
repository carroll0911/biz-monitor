package com.carroll.biz.monitor.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2017/12/5 15:25 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class OperatorDto extends BaseDto {
    @ApiModelProperty(value = "告警人员名称", position = 1)
    private String name;
    @ApiModelProperty(value = "告警人员邮件", position = 2)
    private String email;
    @ApiModelProperty(value = "告警人员手机号码", position = 3)
    private String mobile;
}
