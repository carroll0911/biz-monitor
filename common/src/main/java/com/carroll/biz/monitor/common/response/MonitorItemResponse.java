package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.MonitorItemDto;
import lombok.Data;

/**
 * Created on 2017/12/5 16:50 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class MonitorItemResponse extends MonitorBaseResponse {
    private MonitorItemDto data;
}
