package com.carroll.biz.monitor.analyzer.service;

import com.alibaba.fastjson.JSON;
import com.carroll.biz.monitor.analyzer.consumer.MonitorDataConsumer;
import com.carroll.biz.monitor.analyzer.dto.NotifyDataDto;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.model.WarningData;
import com.carroll.biz.monitor.analyzer.service.impl.ItemSummaryRecordServiceImpl;
import com.carroll.biz.monitor.analyzer.service.impl.MonitorItemServiceImpl;
import com.carroll.biz.monitor.analyzer.service.impl.ProjectServiceImpl;
import com.carroll.biz.monitor.analyzer.service.impl.WarningDataServiceImpl;
import com.carroll.biz.monitor.common.utils.ShareUtils;
import com.carroll.biz.monitor.data.common.dto.MonitorData;
import com.carroll.biz.monitor.data.common.utils.PasswordUtils;
import com.carroll.cache.LockUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Date;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * 数据分析单元测试
 * Created on 202017/11/29 14:36 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@RunWith(PowerMockRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class MonitorDataAnalizeTest {
    @InjectMocks
    private MonitorDataConsumer dataConsumer;

    @Mock
    private MonitorItemServiceImpl monitorItemService;

    @Mock
    private WarningDataServiceImpl warningDataService;

    @Mock
    private ProjectServiceImpl projectService;
    @Mock
    private ItemSummaryRecordServiceImpl itemSummaryRecordService;
    @Mock
    private ShareUtils shareUtils;
    @Mock
    private LockUtils lockUtils;
    @Mock
    private SimpMessagingTemplate template;

    private static final String ITEM_TAG = "tag1";
    private static final String ITEM_ID = "id1";
    private static final String APPLICATION_NAME = "app";
    private static final String HOST = "UNKNOWN";
    private static final String PROJECT_TAG = "project1";
    private static final String PROJECT_ID = "project1";
    private static final String PROJECT_PWD = "project_pwd";

    @Test
    public void test01Failed() {
        long time = System.currentTimeMillis();
        MonitorData monitorData = new MonitorData();
        monitorData.setTag("tag1");
        monitorData.setProjectTag(PROJECT_TAG);
        monitorData.setPassword(PasswordUtils.encodePassword(PROJECT_TAG, PROJECT_PWD));
        monitorData.setApplicationName(APPLICATION_NAME);
        monitorData.setResult(false);
        monitorData.setTime(time);
        mockProject();
        mockMonitorItem();
        mockWarningData();
        mockLock();
        mockSendToEndpoint();
        WarningData data = dataConsumer.analize(JSON.toJSONString(monitorData), true);
        log.info("{}", data.getSuccessTimes());
        Assert.assertTrue(data.getSuccessTimes().equals(0L));
        Assert.assertEquals(data.getStatus(), WarningData.Status.NORMAL);
        Assert.assertTrue(data.getTimes().equals(11L));
    }

    @Test
    public void test02Success() {
        long time = System.currentTimeMillis();
        MonitorData monitorData = new MonitorData();
        monitorData.setTag("tag1");
        monitorData.setProjectTag(PROJECT_TAG);
        monitorData.setPassword(PasswordUtils.encodePassword(PROJECT_TAG, PROJECT_PWD));
        monitorData.setApplicationName(APPLICATION_NAME);
        monitorData.setResult(true);
        monitorData.setTime(time);
        mockProject();
        mockMonitorItem();
        mockWarningData();
        mockLock();
        mockSendToEndpoint();
        when(monitorItemService.isSuccess(any(),any())).thenReturn(true);
        WarningData data = dataConsumer.analize(JSON.toJSONString(monitorData), true);
        log.info("{}", data.getSuccessTimes());
        Assert.assertTrue(data.getSuccessTimes().equals(10L));
        Assert.assertEquals(data.getStatus(), WarningData.Status.CLEARED);
    }

    public void mockWarningData() {
        WarningData warningData = new WarningData();
        warningData.setCycleTimes(0L);
        warningData.setSuccessTimes(9L);
        warningData.setStatus(WarningData.Status.NORMAL);
        warningData.setTimes(10L);
        warningData.setLatestTime(new Date());

        when(warningDataService.getCurrentData(ITEM_ID, APPLICATION_NAME, HOST, null)).thenReturn(warningData);
    }

    public void mockMonitorItem() {
        MonitorItem monitorItem = new MonitorItem();
        monitorItem.setId(ITEM_ID);
        monitorItem.setTag(ITEM_TAG);
        monitorItem.setCycle(3600L);
        monitorItem.setTimes(15L);
        monitorItem.setRecoveryTimes(10L);

        when(monitorItemService.getByTag(ITEM_TAG, PROJECT_ID)).thenReturn(monitorItem);
    }

    public void mockProject() {
        Project project = new Project();
        project.setPassword(PROJECT_PWD);
        project.setTag(PROJECT_TAG);
        project.setId(PROJECT_ID);

        when(projectService.getByTag(PROJECT_TAG)).thenReturn(project);
    }

    public void mockLock(){
        when(lockUtils.lock(anyString(),anyLong())).thenReturn(Boolean.TRUE);
    }

    public void mockSendToEndpoint(){
        doNothing().when(template).convertAndSend(anyString(),any(NotifyDataDto.class));
    }
}
