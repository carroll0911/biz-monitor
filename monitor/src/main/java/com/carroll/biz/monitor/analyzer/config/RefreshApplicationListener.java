package com.carroll.biz.monitor.analyzer.config;

import com.carroll.biz.monitor.analyzer.service.IMonitorItemService;
import com.carroll.biz.monitor.analyzer.service.IProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author: hehongbo
 * @date 2019/1/15
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Component
@Slf4j
public class RefreshApplicationListener implements ApplicationListener<RefreshScopeRefreshedEvent> {

    @Autowired
    private IProjectService projectService;
    @Autowired
    private IMonitorItemService monitorItemService;

    @Override
    public void onApplicationEvent(RefreshScopeRefreshedEvent refreshScopeRefreshedEvent) {
        log.info("......refresh project share data begin......");
        projectService.refreshProject();
        monitorItemService.initSystemMonitorItem();
        log.info("......refresh project share data finished......");
    }
}
