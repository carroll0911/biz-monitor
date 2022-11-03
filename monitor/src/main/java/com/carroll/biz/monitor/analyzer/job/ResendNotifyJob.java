package com.carroll.biz.monitor.analyzer.job;

import com.carroll.biz.monitor.analyzer.service.IWarningDataService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重新发送告警定时任务
 * Created on 2017/11/24 14:41 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Component
public class ResendNotifyJob {
    @Autowired
    private IWarningDataService warningDataService;

    @XxlJob("resendNotifyJob")
    public void execute(String s) throws Exception {
        XxlJobHelper.log("---- resendNotifyJob begin ----");
        warningDataService.resendData();
        XxlJobHelper.log("---- resendNotifyJob end ----");
        XxlJobHelper.handleSuccess();
    }
}
