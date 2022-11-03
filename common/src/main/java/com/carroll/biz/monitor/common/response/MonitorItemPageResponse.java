package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.MonitorItemDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created on 2017/12/5 17:43 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class MonitorItemPageResponse extends PageResponse {
    private List<MonitorItemDto> list;
}
