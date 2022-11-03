package com.carroll.biz.monitor.analyzer.service.impl;

import com.carroll.biz.monitor.analyzer.service.INotifyService;
import com.carroll.biz.monitor.analyzer.dto.NotifyDataDto;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.NotifyRecord;
import com.carroll.biz.monitor.analyzer.model.Operator;
import com.carroll.biz.monitor.analyzer.model.WarningData;
import com.carroll.utils.OldDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警通知--短信实现
 *
 * @author: hehongbo
 * @date 2019/11/8
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Service
@Slf4j
public class NotifyService4SmsImpl extends NotifyServiceAbstract implements INotifyService {

    @Autowired
    private SmsWsClient smsWsClient;

    @Override
    public MonitorItem.MessageType getMessageType() {
        return MonitorItem.MessageType.SMS;
    }

    @Override
    public void notify(NotifyDataDto data) {
        List<String> toList = new ArrayList<>();
        for (Operator operator : data.getMonitorItem().getReceivers()) {
            if (!StringUtils.isEmpty(operator.getMobile())) {
                toList.add(operator.getMobile());
            }
        }
        if (toList.isEmpty()) {
            log.warn("监控项：{}接收人手机号为空");
            return;
        }
        //告警通知：【C3数据服务平台】监控系统告警通知：系统于2017-09-09 12:00:00 产生新的告警信息，请及时处理。【告警内容】 MySQL访问失败。【告警级别】紧急。【告警次数】1次。
        String formatWarnStr = "%s//%s//%s//%s//%s";
        //恢复告警通知：【C3数据服务平台】监控系统告警恢复通知：【告警内容】 MySQL访问失败。【恢复时间】2017-09-09 12:00:00。
        String formatrecoveryStr = "%s//%s//%s";
        //过期告警清除通知：【C3数据服务平台】监控系统告警过期告警清除通知：系统自动清除逾期未处理告警。【告警内容】 MySQL访问失败。【产生时间】2017-09-09 12:00:00。
        String formatCleanStr = "%s//%s//%s";

        String templateCode = null;
        StringBuilder paramsBuild = new StringBuilder();
        String params = null;
        WarningData warningData = data.getWarningData();
        MonitorItem monitorItem = data.getMonitorItem();
        NotifyRecord.NotifyType notifyType = NotifyRecord.NotifyType.WARN;
        for (String to : toList) {
            if (paramsBuild.length() > 0) {
                paramsBuild.append("#");
            }
            if (WarningData.Status.NORMAL.equals(data.getWarningData().getStatus())) {
                if (data.getWarningData().getLastSendTime() == null) {
                    params = String.format(formatWarnStr, to, OldDateUtils.getStrTimeFormat(warningData.getFirstTime()), monitorItem.getName(), monitorItem.getLevel().getDesc(), warningData.getTimes());
                    templateCode = SmsWsClient.TEMPLATE_CODE_WARN;
                } else {
                    params = String.format(formatWarnStr, to, OldDateUtils.getStrTimeFormat(warningData.getLatestTime()), monitorItem.getName(), monitorItem.getLevel().getDesc(), warningData.getTimes());
                    templateCode = SmsWsClient.TEMPLATE_CODE_RE_WARN;
                }

            } else {
                if (warningData.getRecoveryTime() != null) {
                    params = String.format(formatrecoveryStr, to, monitorItem.getName(), OldDateUtils.getStrTimeFormat(warningData.getRecoveryTime()));
                    templateCode = SmsWsClient.TEMPLATE_CODE_RECOVERY;
                    notifyType = NotifyRecord.NotifyType.RECOVERY;
                } else {
                    params = String.format(formatCleanStr, to, monitorItem.getName(), OldDateUtils.getStrTimeFormat(warningData.getFirstTime()));
                    templateCode = SmsWsClient.TEMPLATE_CODE_CLEAN;
                }

            }
            paramsBuild.append(params);
        }
        boolean success = smsWsClient.sendMsg(paramsBuild.toString(), templateCode);
        NotifyRecord record = new NotifyRecord();
        record.setNotifyType(notifyType);
        record.setContent(paramsBuild.toString());
        record.setTemplateNo(templateCode);
        saveRecord(data, success, record, MonitorItem.MessageType.SMS);
    }

}
