package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.WarningDataCallBackDto;
import lombok.Data;

/**
 * Created on 2017/12/14 11:31 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class WarnDataCallBackResponse extends MonitorBaseResponse {
    private WarningDataCallBackDto data;
}
