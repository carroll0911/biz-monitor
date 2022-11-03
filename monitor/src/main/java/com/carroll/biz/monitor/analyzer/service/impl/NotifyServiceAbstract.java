package com.carroll.biz.monitor.analyzer.service.impl;

import com.carroll.biz.monitor.analyzer.repository.NotifyRecordRepository;
import com.carroll.biz.monitor.analyzer.service.INotifyService;
import com.carroll.biz.monitor.analyzer.dto.NotifyDataDto;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.NotifyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author: hehongbo
 * @date 2019/11/8
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
public abstract class NotifyServiceAbstract implements INotifyService {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private NotifyRecordRepository notifyRecordRepository;

    @PostConstruct
    public void regist() {
        NotifyServiceRegister.regist(context.getBean(this.getClass()));
    }

    protected void saveRecord(NotifyDataDto notifyDataDto, boolean success, NotifyRecord record, MonitorItem.MessageType msgType) {
        record.setMessageType(msgType);
        record.setSendTime(new Date());
        record.setReceivers(notifyDataDto.getMonitorItem().getReceivers());
        record.setSuccess(success);
        record.setWarnData(notifyDataDto.getWarningData());
        try {
            notifyRecordRepository.save(record);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
