package com.carroll.biz.monitor.analyzer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: hehongbo
 * @date 2019/9/29
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@ApiModel
@Getter
@Setter
public class ProjectDetailDto {

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("应用密码")
    private String password;
    @ApiModelProperty("应用标签")
    private String tag;
    @ApiModelProperty("应用授权用户列表")
    private List<OperatorDtoV2> users;
}
