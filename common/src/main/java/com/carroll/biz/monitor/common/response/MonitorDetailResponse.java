package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.WarningDataDetailDto;
import lombok.Data;

/**
 * Created on 2017/11/30 13:17 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class MonitorDetailResponse extends MonitorBaseResponse {

    private WarningDataDetailDto data;
}
