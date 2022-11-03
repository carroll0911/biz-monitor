package com.carroll.biz.monitor.analyzer.dto;

import com.carroll.biz.monitor.analyzer.enums.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: hehongbo
 * @date 2019/9/29
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@ApiModel
@Getter
@Setter
public class OperatorDtoV2 {
    private String id;
    @ApiModelProperty(value = "告警人员名称", position = 1)
    private String name;
    @ApiModelProperty(value = "告警人员邮件", position = 2)
    private String email;
    @ApiModelProperty(value = "告警人员手机号码", position = 3)
    private String mobile;
    @ApiModelProperty(value = "角色", position = 4)
    private Role role;
}
