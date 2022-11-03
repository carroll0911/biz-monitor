package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.WarningDataPageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created on 2017/11/30 11:21 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class HistoryMonitorPageResponse extends PageResponse {

    private List<WarningDataPageDto> list;
}
