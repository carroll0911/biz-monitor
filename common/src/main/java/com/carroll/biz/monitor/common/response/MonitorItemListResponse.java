package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.MonitorItemListDto;
import lombok.Data;

import java.util.List;

/**
 * Created on 2017/12/18 14:06 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class MonitorItemListResponse extends MonitorBaseResponse {
    private List<MonitorItemListDto> list;
}
