package com.carroll.biz.monitor.analyzer.response;

import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: hehongbo
 * @date 2019/12/3
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class TraceDetailResponse extends MonitorBaseResponse {

    private Object data;
}
