package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.Operator;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.model.UserProject;
import com.carroll.biz.monitor.analyzer.repository.MonitorItemRepository;
import com.carroll.biz.monitor.analyzer.repository.OperatorRepository;
import com.carroll.biz.monitor.analyzer.repository.ProjectRepository;
import com.carroll.biz.monitor.analyzer.repository.UserProjectRepository;
import com.carroll.biz.monitor.analyzer.service.impl.MonitorItemServiceImpl;
import com.carroll.biz.monitor.analyzer.service.impl.ProjectServiceImpl;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import com.carroll.biz.monitor.analyzer.utils.PageUtil;
import com.carroll.biz.monitor.common.dto.BaseDto;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.request.*;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.MonitorItemListResponse;
import com.carroll.biz.monitor.common.response.MonitorItemPageResponse;
import com.carroll.biz.monitor.common.response.MonitorItemResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.function.Function;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created on 2017/12/12 16:13 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@RunWith(PowerMockRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
@PrepareForTest({BizContext.class})
public class MonitorItemServiceTest {

    @InjectMocks
    private MonitorItemServiceImpl monitorItemService;

    @Mock
    private MonitorItemRepository monitorItemRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserProjectRepository userProjectRepository;
    @Mock
    private ProjectServiceImpl projectService;

    private static final String TAG = "test";
    private static final String PROJECT_ID = "5a163b834668c04574f962ea";

    public MonitorItemServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test01GetByTag() {
        mockMonitorItemByTag();
        MonitorItem item = monitorItemService.getByTag(TAG, PROJECT_ID);
        Assert.assertNotNull(item);
        Assert.assertEquals("UT", item.getName());
    }

    @Test
    public void test02Save() {
        MonitorItem item = new MonitorItem();
        item.setName("test3");
        item.setTag("test");
        item.setCycle(5L);
        List<MonitorItem.MessageType> msgType = new ArrayList<>();
        msgType.add(MonitorItem.MessageType.EMAIL);
        item.setMsgTypes(msgType);
        item.setCategory(MonitorItem.Category.BUSINESS);
        item.setLevel(MonitorItem.Level.CRITICAL);
        item.setSendFlag(true);
        item.setProjectId("5a163b834668c04574f962ea");
        monitorItemService.save(item);
    }

    @Test
    public void test03Detail() throws BaseException {
        IdRequest request = new IdRequest();
        request.setId("5a1bb9124e58db1c5c1f08ae");
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        //item is null
        when(monitorItemRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
        mockUserCache();
        try {
            monitorItemService.detail(request);
        } catch (BaseException e) {
            Assert.assertEquals("project not exist", e.getReturnErrCode(), ErrEnum.DATA_NOT_EXIST.getCode());
        }
        //projectId is not equal
        mockMonitorItem();
        request.setId("5a1bb9124e58db1c5c1f08ae");
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        mockMonitorItem();
        try {
            monitorItemService.detail(request);
        } catch (BaseException e) {
            Assert.assertEquals("project not exist", e.getReturnErrCode(), ErrEnum.PROJECT_ID_ERROR.getCode());
        }

        //success
        request.setId("5a1bb9124e58db1c5c1f08ae");
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        mockMonitorItem();
        Project project = new Project();
        project.setName("C3");
        project.setTag("c3");
        project.setPassword("123qweASD0");
        when(projectRepository.findById(anyString())).thenReturn(Optional.ofNullable(project));

        MonitorItemResponse response = monitorItemService.detail(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getData());
        Assert.assertEquals("UT", response.getData().getTag());
    }

    public void mockMonitorItem() {
        MonitorItem item = new MonitorItem();
        item.setId("5a1bb9124e58db1c5c1f08ae");
        item.setProjectId("5a163b834668c04574f962ea");
        item.setName("UT");
        item.setTag("UT");
        item.setCategory(MonitorItem.Category.SYSTEM);
        item.setLevel(MonitorItem.Level.WARNING);
        List<MonitorItem.MessageType> msgType = new ArrayList<>();
        msgType.add(MonitorItem.MessageType.SMS);
        item.setMsgTypes(msgType);
        when(monitorItemRepository.findById(anyString())).thenReturn(Optional.ofNullable(item));
    }

    @Test
    public void test04Page() {
        MonitorItemPageRequest request = new MonitorItemPageRequest();
        request.setCurPage(0);
        request.setPageSize(10);
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        //page is null
        when(monitorItemRepository.advanceQuery(request.getProjectId(), PageUtil.convertPageRequestToPageable(request))).thenReturn(null);
        Project project = new Project();
        project.setName("C3");
        project.setTag("c3");
        project.setPassword("123qweASD0");
        when(projectRepository.findById(anyString())).thenReturn(Optional.ofNullable(project));
        MonitorItemPageResponse response = monitorItemService.page(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getList().size() == 0);

        //page is not null
        Page<MonitorItem> page = new Page<MonitorItem>() {
            @Override
            public int getTotalPages() {
                return 10;
            }

            @Override
            public long getTotalElements() {
                return 10;
            }

            @Override
            public <U> Page<U> map(Function<? super MonitorItem, ? extends U> converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return 10;
            }

            @Override
            public int getSize() {
                return 10;
            }

            @Override
            public int getNumberOfElements() {
                return 1;
            }

            @Override
            public List<MonitorItem> getContent() {
                List<MonitorItem> list = new ArrayList<>();
                MonitorItem item = new MonitorItem();
                item.setId("5a1bb9124e58db1c5c1f08ae");
                item.setProjectId("5a163b834668c04574f962ea");
                item.setName("UT");
                item.setTag("UT");
                item.setCategory(MonitorItem.Category.SYSTEM);
                item.setLevel(MonitorItem.Level.WARNING);
                List<MonitorItem.MessageType> msgType = new ArrayList<>();
                msgType.add(MonitorItem.MessageType.SMS);
                item.setMsgTypes(msgType);
                list.add(item);
                return list;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator<MonitorItem> iterator() {
                return null;
            }
        };

        mockUserCache();
        when(monitorItemRepository.advanceQuery(anyList(), anyString(), any(Pageable.class))).thenReturn(page);
        response = monitorItemService.page(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getList());
        Assert.assertTrue(response.getList().size() == 1);
    }

    @Test
    public void test05Update() throws BaseException {
        MonitorItemUpdateRequest request = new MonitorItemUpdateRequest();
        request.setId("5a1bb9124e58db1c5c1f08ae1");
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        request.setCategory(MonitorItem.Category.SYSTEM.name());
        request.setLevel(MonitorItem.Level.WARNING.name());
        mockMonitorItem();
        Project project = new Project();
        project.setName("C3");
        project.setTag("c3");
        project.setPassword("123qweASD0");
        when(projectRepository.findById(anyString())).thenReturn(Optional.ofNullable(project));

        mockUserCache();

        MonitorItemResponse response = monitorItemService.update(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getData());
        Assert.assertEquals(MonitorItem.Category.SYSTEM.getDesc(), response.getData().getCategory());
    }

    @Test
    public void test06Notify() throws BaseException {
        MonitorItemNotifyRequest request = new MonitorItemNotifyRequest();
        List<BaseDto> operatorList = new ArrayList<>();
        BaseDto dto = new BaseDto();
        dto.setId("5a336a814e58db1238b00a25");
        operatorList.add(dto);

        request.setId("5a1bb9124e58db1c5c1f08ae");
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        request.setSendFlag(true);
        List<String> msgType = new ArrayList<>();
        msgType.add(MonitorItem.MessageType.SMS.name());
        request.setMsgTypes(msgType);
        request.setCycle(5L);
        request.setCycleTimes(10L);
        request.setRecoveryTimes(10L);
        request.setReceivers(operatorList);

        MonitorItem item = new MonitorItem();
        item.setTag("test");
        item.setName("UT");
        item.setProjectId("5a163b834668c04574f962ea");
        item.setCycle(10L);
        when(monitorItemRepository.findById(anyString())).thenReturn(Optional.ofNullable(item));


        Operator operator = new Operator();
//        operator.setProjectId("5a163b834668c04574f962ea");
        operator.setMobile("13011112345");
        operator.setName("sfasd");
        operator.setEmail("1234567876554@qq.com");
        when(operatorRepository.findById("5a336a814e58db1238b00a25")).thenReturn(Optional.ofNullable(operator));
        Project project = new Project();
        project.setName("C3");
        project.setTag("c3");
        project.setPassword("123qweASD0");
        project.setId("5a163b834668c04574f962ea");
        when(projectRepository.findById(anyString())).thenReturn(Optional.ofNullable(project));
        when(userProjectRepository.findAllByProjectId(eq("5a163b834668c04574f962ea")))
                .thenReturn(Arrays.asList(new UserProject("5a163b834668c04574f962ea", "5a336a814e58db1238b00a25", Role.NORMAL)));
        MonitorBaseResponse response = monitorItemService.notify(request);
        Assert.assertNotNull(response);
    }

    public void mockMonitorItemByTag() {
        MonitorItem item = new MonitorItem();
        item.setTag(TAG);
        item.setName("UT");
        item.setProjectId(PROJECT_ID);
        when(monitorItemRepository.findTopByTagAndProjectId(TAG, PROJECT_ID)).thenReturn(item);
    }

    @Test
    public void test07List() {
        BmBaseRequest request = new BmBaseRequest();
        //project is null
        request.setProjectId("5a163b834668c04574f962eb");
        monitorItemService.list(request);
        //!project.getTag().equals(projectTag)
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c4");
        monitorItemService.list(request);

        //success
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");

        List<MonitorItem> list = new ArrayList<>();
        MonitorItem item = new MonitorItem();
        item.setName("TEST");
        item.setProjectId("5a1fd7544e58db09149115af");
        item.setCategory(MonitorItem.Category.BUSINESS);
        item.setLevel(MonitorItem.Level.CRITICAL);
        List<MonitorItem.MessageType> msgType = new ArrayList<>();
        msgType.add(MonitorItem.MessageType.EMAIL);
        item.setMsgTypes(msgType);
        item.setCycle(10L);
        item.setCycleTimes(10L);
        item.setDescription("UT test");
        item.setRecoveryTimes(10L);
        item.setSuggest("UT test");
        item.setTag("UT");
        item.setTimes(10L);
        item.setId("5a1bb9124e58db1c5c1f08ae");
        list.add(item);

        when(monitorItemRepository.findByProjectIdIsIn(anyList())).thenReturn(list);
        UserCacheDto dto = new UserCacheDto();
        dto.setRole(Role.SUPPER);
        PowerMockito.mockStatic(BizContext.class);
        when(BizContext.getData(anyString())).thenReturn(dto);

        Project project = new Project();
        project.setName("C3");
        project.setTag("c3");
        project.setId("c3");
        project.setPassword("123qweASD0");
        when(projectRepository.findById(anyString())).thenReturn(Optional.ofNullable(project));
        MonitorItemListResponse response = monitorItemService.list(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getList().size() == 1);
    }

    @Test
    public void test08Save() throws BaseException {
        MonitorItemRequest request = new MonitorItemRequest();
        request.setName("TEST");
        request.setProjectId("5a1fd7544e58db09149115af");
        request.setCategory(MonitorItem.Category.BUSINESS.name());
        request.setLevel(MonitorItem.Level.CRITICAL.name());
        List<String> msgType = new ArrayList<>();
        msgType.add(MonitorItem.MessageType.EMAIL.name());
        request.setMsgTypes(msgType);
        request.setCycle(10L);
        request.setCycleTimes(10L);
        request.setDescription("UT test");
        request.setRecoveryTimes(10L);
        request.setSuggest("UT test");
        request.setTag("UT");
        request.setTimes(10L);

        mockUserCache();
        //project is null
        when(projectRepository.findById(request.getProjectId())).thenReturn(Optional.ofNullable(null));
        try {
            monitorItemService.save(request);
        } catch (BaseException e) {
            Assert.assertEquals("project not exist", e.getReturnErrCode(), ErrEnum.PROJECT_NOT_EXIST.getCode());
        }

        //cycle%5 != 0
        request.setCycle(1L);
        mockProject();
        try {
            monitorItemService.save(request);
        } catch (BaseException e) {
            Assert.assertEquals("monitoritem_cycle_error", e.getReturnErrCode(), ErrEnum.MONITORITEM_CYCLE_ERROR.getCode());
        }

        //operator not exist
        request.setCycle(10L);
        mockProject();
        List<BaseDto> operatorIds = new ArrayList<>();
        BaseDto id = new BaseDto();
        id.setId("5a336a814e58db1238b00a25");
        operatorIds.add(id);
        request.setReceivers(operatorIds);
        try {
            monitorItemService.save(request);
        } catch (BaseException e) {
            Assert.assertEquals("operator_not_exist", e.getReturnErrCode(), ErrEnum.OPERATOR_NOT_EXIST.getCode());
        }

        //project id is not equal
        mockOperator("5a1fd7544e58db09149115ag");
        try {
            monitorItemService.save(request);
        } catch (BaseException e) {
            Assert.assertEquals("operator_project_not_match", e.getReturnErrCode(), ErrEnum.OPERATOR_PROJECT_NOT_MATCH.getCode());
        }

        //success
        mockOperator("5a1fd7544e58db09149115af");
        MonitorItemResponse response = monitorItemService.save(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getReturnSuccess());
    }

    private void mockProject() {
        Project project = new Project();
        project.setName("C4");
        project.setTag("C4");
        project.setPassword("123qweASD0");
        when(projectRepository.findById(anyString())).thenReturn(Optional.ofNullable(project));
    }

    private void mockOperator(String projectId) {
        Operator operator = new Operator();
        operator.setName("张三");
        operator.setMobile("13099992222");
        operator.setEmail("123@qq.com");
//        operator.setProjectId(projectId);
        when(operatorRepository.findById(anyString())).thenReturn(Optional.ofNullable(operator));
        when(userProjectRepository.findAllByProjectId(eq(projectId)))
                .thenReturn(Arrays.asList(new UserProject(projectId, "5a336a814e58db1238b00a25", Role.NORMAL)));
    }

    private void mockUserCache() {
        UserCacheDto dto = new UserCacheDto();
        dto.setRole(Role.SUPPER);
        PowerMockito.mockStatic(BizContext.class);
        when(BizContext.getData(anyString())).thenReturn(dto);
    }

}
