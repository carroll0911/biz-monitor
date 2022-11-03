package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.OperatorDto;
import lombok.Data;

import java.util.List;

/**
 * Created on 2017/12/19 15:04 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class OperatorListResponse extends MonitorBaseResponse {

    private List<OperatorDto> list;
}
