package com.carroll.biz.monitor.analyzer.dto;

import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.WarningData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 告警通知数据模型
 * Created on 202017/11/23 13:24 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDataDto {

    private boolean recovery;
    private MonitorItem monitorItem;
    private WarningData warningData;
}
