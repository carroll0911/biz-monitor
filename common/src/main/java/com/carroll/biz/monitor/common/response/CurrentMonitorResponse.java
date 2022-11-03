package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.CurrentMonitorDto;
import com.carroll.biz.monitor.common.dto.WarningDataStatisticDto;
import lombok.Data;

import java.util.List;

/**
 * Created on 2017/12/19 9:42 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class CurrentMonitorResponse extends PageResponse {
    private List<WarningDataStatisticDto> statisticList;

    private List<CurrentMonitorDto> list;
}
