package com.carroll.biz.monitor.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2017/12/6 17:26 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class BmPageRequest extends PageRequest {

    @ApiModelProperty(value = "所属项目id", required = true)
//    @NotBlank(message = "项目id不能为空")
    private String projectId;

    @ApiModelProperty(value = "所属项目tag", required = true)
//    @NotBlank(message = "项目tag不能为空")
    private String projectTag;
}
