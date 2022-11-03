package com.carroll.biz.monitor.analyzer.response;

import com.carroll.biz.monitor.analyzer.dto.MonitorTargetDto;
import com.carroll.biz.monitor.common.response.PageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 告警对象分页返回对象
 *
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class MonitorTargetPageResponse extends PageResponse {

    private List<MonitorTargetDto> list;
}
