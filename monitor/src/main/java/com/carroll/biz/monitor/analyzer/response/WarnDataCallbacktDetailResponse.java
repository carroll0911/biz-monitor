package com.carroll.biz.monitor.analyzer.response;

import com.carroll.biz.monitor.analyzer.dto.WarnDataCallBackDto;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import io.swagger.annotations.ApiModel;
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
public class WarnDataCallbacktDetailResponse extends MonitorBaseResponse {

    private WarnDataCallBackDto data;
}
