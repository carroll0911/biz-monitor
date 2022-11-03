package com.carroll.biz.monitor.analyzer.response;

import com.carroll.biz.monitor.analyzer.model.MonitorTarget;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
@ApiModel
public class MonitorTargetDetailResponse extends MonitorBaseResponse {

    private MonitorTarget data;
}
