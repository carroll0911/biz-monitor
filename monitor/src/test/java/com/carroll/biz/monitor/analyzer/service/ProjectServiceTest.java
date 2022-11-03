package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.enums.ErrEnum;
import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.repository.ProjectRepository;
import com.carroll.biz.monitor.analyzer.repository.UserProjectRepository;
import com.carroll.biz.monitor.analyzer.service.impl.ProjectServiceImpl;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import com.carroll.biz.monitor.common.request.ProjectRequest;
import com.carroll.biz.monitor.common.request.ProjectUpdateRequest;
import com.carroll.biz.monitor.common.response.ProjectResponse;
import com.carroll.biz.monitor.common.utils.ShareUtils;
import com.carroll.spring.rest.starter.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Created on 2017/12/13 15:17 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@RunWith(PowerMockRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
@PrepareForTest({BizContext.class})
public class ProjectServiceTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ShareUtils shareUtils;
    @Mock
    private UserProjectRepository userProjectRepository;

    public ProjectServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test01GetByTag() {
        this.mockProjectByTag();
        Project project = projectService.getByTag("test");
        Assert.assertNotNull(project);
        Assert.assertEquals("test", project.getTag());
    }

    public void mockProjectByTag() {
        Project project = new Project();
        project.setName("UT测试");
        project.setTag("test");
        project.setPassword("d12bd30cceabf4406ac801ad5207caa4");
        when(projectRepository.findByTag("test")).thenReturn(project);
    }

    @Test
    public void test02Save() throws BaseException {
        ProjectRequest request = new ProjectRequest();
        request.setName("TEST01");
        request.setTag("TEST");

        mockProject();
        mockShareProject();
        mockUserCache();
        ProjectResponse response = projectService.save(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getData());
        Assert.assertEquals(response.getData().getName(), "TEST01");
    }

    private void mockUserCache(){
        UserCacheDto dto=new UserCacheDto();
        dto.setRole(Role.SUPPER);
        PowerMockito.mockStatic(BizContext.class);
        when(BizContext.getData(anyString())).thenReturn(dto);
    }

    public void mockProject() {
        Project project = new Project();
        project.setName("TEST01");
        project.setTag("TEST");
        project.setPassword("123qwe456A");
        when(projectRepository.save(project)).thenReturn(project);
    }

    @Test
    public void test03Update() throws BaseException {
        ProjectUpdateRequest request = new ProjectUpdateRequest();
        request.setId("12345678909");
        request.setName("TEST02");
        request.setTag("TEST");
        mockShareProject();
        when(projectRepository.findById("12345678909")).thenReturn(Optional.ofNullable(null));
        mockProject();
        mockUserCache();
        try {
            projectService.update(request);
        } catch (BaseException e) {
            Assert.assertEquals("project not exist", e.getReturnErrCode(), ErrEnum.DATA_NOT_EXIST.getCode());
        }

        Project project = new Project();
        project.setName("TEST01");
        project.setTag("TEST");
        project.setPassword("123qwe456A");

        when(projectRepository.findById("12345678909")).thenReturn(Optional.ofNullable(project));
        mockProject();
        ProjectResponse response = projectService.update(request);

        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getData());
        Assert.assertEquals(response.getData().getName(), "TEST02");
    }

    private void mockShareProject() {
        doNothing().when(shareUtils).shareProject(any());
    }
}
