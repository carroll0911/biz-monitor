package com.carroll.biz.monitor.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * Created on 2017/12/15 10:48 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class ProjectUpdateRequest {

    @ApiModelProperty(value = "id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "tag", required = true)
    @NotBlank(message = "tag不能为空")
    private String tag;

    /**
     * 用户ID列表
     */
    @ApiModelProperty(value = "用户id列表")
    private List<String> users;

    /**
     * 管理员用户id列表
     */
    @ApiModelProperty(value = "管理员用户id列表")
    private List<String> admins;
}
