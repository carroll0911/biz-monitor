package com.carroll.biz.monitor.common.request;

import com.carroll.spring.rest.starter.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2017/12/6 17:23 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class BmBaseRequest extends BaseRequest {

    @ApiModelProperty(value = "所属项目id")
    private String projectId;

    @ApiModelProperty(value = "所属项目tag")
    private String projectTag;
}
