package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.WarningDataStatisticDto;
import lombok.Data;

import java.util.List;

/**
 * Created on 2017/11/30 16:26 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class MonitorStatisticResponse extends MonitorBaseResponse {

    private List<List<WarningDataStatisticDto>> list;
}
