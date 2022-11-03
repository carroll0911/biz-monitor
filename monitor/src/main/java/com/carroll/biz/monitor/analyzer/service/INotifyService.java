package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.dto.NotifyDataDto;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;

/**
 * 告警通知接口
 *
 * @author: hehongbo
 * @date 2019/11/8
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public interface INotifyService {

    MonitorItem.MessageType getMessageType();

    void notify(NotifyDataDto data);
}
