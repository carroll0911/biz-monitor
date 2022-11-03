package com.carroll.biz.monitor.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2017/12/8 16:16 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class ProjectDto extends BaseDto {
    @ApiModelProperty(value = "名称", position = 1)
    private String name;
    @ApiModelProperty(value = "tag", position = 2)
    private String tag;
    @ApiModelProperty(value = "密码", position = 3)
    private String password;
}
