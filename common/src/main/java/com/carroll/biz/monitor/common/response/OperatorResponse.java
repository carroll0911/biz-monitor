package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.OperatorDto;
import lombok.Data;

/**
 * Created on 2017/12/5 15:25 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class OperatorResponse extends MonitorBaseResponse {
    private OperatorDto data;
}
