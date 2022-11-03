package com.carroll.biz.monitor.analyzer.consumer;

import com.alibaba.fastjson.JSONObject;
import com.carroll.biz.monitor.analyzer.dto.NotifyDataDto;
import com.carroll.biz.monitor.analyzer.service.IMonitorTargetService;
import com.carroll.biz.monitor.analyzer.service.INotifyService;
import com.carroll.biz.monitor.analyzer.service.impl.NotifyServiceRegister;
import com.carroll.biz.monitor.data.common.dto.KafkaTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 告警通知 消费者
 * Created on 202017/11/23 13:43 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Component
@Slf4j
public class NotifyConsumer {

    @Autowired
    private IMonitorTargetService monitorTargetService;
//    private ExecutorService fixedThreadPool = ;

    @KafkaListener(topics = {KafkaTopic.NOTIFY_DATA})
    @SuppressWarnings("unused")
    public void notify(ConsumerRecord<?, ?> cr) {
        Optional<?> messages = Optional.ofNullable(cr.value());
        messages.ifPresent(m -> {
            String content = m.toString();
            log.debug("NotifyConsumer message:" + content);
            NotifyDataDto dataDto = JSONObject.parseObject(content, NotifyDataDto.class);
            if (!needNotify(dataDto) || !dataDto.getMonitorItem().getSendFlag()) {
                return;
            }
            // 根据 告警对象模块维护的内容将告警对象的code转换为name
            if (dataDto.getWarningData() != null && !StringUtils.isEmpty(dataDto.getWarningData().getTarget())) {
                String targetName = monitorTargetService.getTargetName(dataDto.getWarningData().getTarget(),
                        dataDto.getWarningData().getProjectId());
                if (!StringUtils.isEmpty(targetName)) {
                    dataDto.getWarningData().setTarget(targetName);
                }
            }
            dataDto.getMonitorItem().getMsgTypes().forEach(msgType -> {
                INotifyService notifyService = NotifyServiceRegister.listener(msgType);
                if (notifyService != null) {
                    notifyService.notify(dataDto);
                }
            });

        });

    }

    /* 判断是否需要发送告警通知  */
    private boolean needNotify(NotifyDataDto data) {
        if (data.getMonitorItem() == null) {
            return false;
        }
        if (data.getMonitorItem().getReceivers() == null || data.getMonitorItem().getReceivers().isEmpty()) {
            log.warn("监控项：{}未配置告警接收人");
            return false;
        }
        if (data.getWarningData() == null) {
            log.warn("告警数据有误");
            return false;
        }
        return true;
    }

}
