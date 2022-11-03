package com.carroll.biz.monitor.analyzer.job;

import com.carroll.biz.monitor.analyzer.service.IWarningDataService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 清除僵尸数据定时任务
 * Created on 2017/11/24 15:24 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */

@Slf4j
@Component
public class ClearUselessDataJob {

    @Autowired
    private IWarningDataService warningDataService;

    @XxlJob("clearUselessDataJob")
    public void clearUselessData(String s) throws Exception {
        XxlJobHelper.log("----  clearUselessDataJob begin  ----");
        warningDataService.clearUselessData();
        XxlJobHelper.log("----  clearUselessDataJob end  ----");
        XxlJobHelper.handleSuccess();
    }
}
