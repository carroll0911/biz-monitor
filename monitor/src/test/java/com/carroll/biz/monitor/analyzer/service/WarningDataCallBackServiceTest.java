package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.model.WarningDataCallBack;
import com.carroll.biz.monitor.analyzer.repository.ProjectRepository;
import com.carroll.biz.monitor.analyzer.repository.WarnDataCallBackRepository;
import com.carroll.biz.monitor.analyzer.service.impl.WarningDataCallBackServiceImpl;
import com.carroll.biz.monitor.common.request.WarnDataCallBackRequest;
import com.carroll.biz.monitor.common.response.WarnDataCallBackResponse;
import com.carroll.spring.rest.starter.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * Created on 2017/11/27 14:38 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@RunWith(PowerMockRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class WarningDataCallBackServiceTest {


    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private WarnDataCallBackRepository wdcbRepository;

    @InjectMocks
    private WarningDataCallBackServiceImpl warningDataCallBackService;

    @Test
    public void createCallBack() throws BaseException {
        Project project = new Project();
        project.setName("UT测试");
        project.setTag("test");
        project.setId("testId");
        project.setPassword("d12bd30cceabf4406ac801ad5207caa4");
        WarnDataCallBackRequest request =new WarnDataCallBackRequest();
        request.setProjectId(project.getId());
        request.setCallback("http://www.test.com");
        request.setEnable(true);
        when(projectRepository.findById(request.getProjectId())).thenReturn(Optional.ofNullable(project));
        when(wdcbRepository.findByProjectTag(project.getTag())).thenReturn(null);
        WarnDataCallBackResponse response = warningDataCallBackService.save(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getReturnSuccess());
    }

    @Test
    public void updateCallBack() throws BaseException {
        Project project = new Project();
        project.setName("UT测试");
        project.setTag("test");
        project.setId("testId");
        project.setPassword("d12bd30cceabf4406ac801ad5207caa4");
        WarnDataCallBackRequest request =new WarnDataCallBackRequest();
        request.setProjectId("testId");
        request.setCallback("http://www.test.com");
        request.setEnable(true);
        WarningDataCallBack warningDataCallBack=new WarningDataCallBack();
        warningDataCallBack.setCallback("http://www.test1.com");
        warningDataCallBack.setEnable(false);
        warningDataCallBack.setProjectTag("test");
        when(projectRepository.findById(project.getId())).thenReturn(Optional.ofNullable(project));
        when(wdcbRepository.findByProjectTag(project.getTag())).thenReturn(warningDataCallBack);
        WarnDataCallBackResponse response = warningDataCallBackService.save(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getReturnSuccess());
    }

}
