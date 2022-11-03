package com.carroll.biz.monitor.analyzer.request;

import com.carroll.biz.monitor.common.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 告警对象分页请求
 *
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
@ApiModel
public class MonitorTargetPageRequest extends PageRequest {
    @ApiModelProperty("应用ID")
    private String projectId;
    @ApiModelProperty("搜索关键字")
    private String keyword;
}
