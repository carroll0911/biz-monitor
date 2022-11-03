package com.carroll.biz.monitor.analyzer.dto;

import com.carroll.biz.monitor.analyzer.model.Project;
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
public class ProjectListDto {

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("应用标签")
    private String tag;
    @ApiModelProperty("管理员列表")
    private List<OperatorDtoV2> admins;
    @ApiModelProperty("状态")
    private Project.Status status;
}
