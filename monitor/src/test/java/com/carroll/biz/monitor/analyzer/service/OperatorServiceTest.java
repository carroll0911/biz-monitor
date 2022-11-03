package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.Operator;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.repository.MonitorItemRepository;
import com.carroll.biz.monitor.analyzer.repository.OperatorRepository;
import com.carroll.biz.monitor.analyzer.repository.ProjectRepository;
import com.carroll.biz.monitor.analyzer.repository.UserProjectRepository;
import com.carroll.biz.monitor.analyzer.service.impl.OperatorServiceImpl;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import com.carroll.biz.monitor.analyzer.utils.EmailUtils;
import com.carroll.biz.monitor.analyzer.utils.PageUtil;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.request.*;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.OperatorListResponse;
import com.carroll.biz.monitor.common.response.OperatorPageResponse;
import com.carroll.biz.monitor.common.response.OperatorResponse;
import com.carroll.spring.rest.starter.BaseException;
import com.carroll.utils.StringUtil;
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

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created on 2017/12/13 13:56 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@RunWith(PowerMockRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
@PrepareForTest({BizContext.class, StringUtil.class})
public class OperatorServiceTest {
    @InjectMocks
    private OperatorServiceImpl operatorService;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private MonitorItemRepository monitorItemRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private UserProjectRepository userProjectRepository;
    @Mock
    private EmailUtils emailUtils;

    private static final String PROJECT_TAG = "project1";
    private static final String PROJECT_ID = "project1";
    private static final String PROJECT_PWD = "project_pwd";

    public OperatorServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test01Save() throws BaseException {
        OperatorRequest request = new OperatorRequest();
        request.setProjectId(PROJECT_ID);
        request.setProjectTag(PROJECT_TAG);
        request.setName("lily");
        request.setMobile("13012341234");
        request.setEmail("li.lan@timanetworks.com");

        Operator operator = new Operator();
        operator.setName("zhangsan");
        operator.setEmail("l@timanetworks.com");
        operator.setMobile("13011111111");
//        operator.setProjectId(PROJECT_ID);
        mockProject();
        when(operatorRepository.save(operator)).thenReturn(operator);
        mockUserCache();
        PowerMockito.mockStatic(StringUtil.class);
        when(StringUtil.generatePwd()).thenReturn(PROJECT_PWD);
        OperatorResponse response = operatorService.save(request);
        Assert.assertNotNull(response);
    }

    private void mockUserCache(){
        UserCacheDto dto = new UserCacheDto();
        dto.setRole(Role.SUPPER);
        PowerMockito.mockStatic(BizContext.class);
        when(BizContext.getData(anyString())).thenReturn(dto);
    }

    @Test
    public void test02Update() throws BaseException {
        OperatorUpdateRequest request = new OperatorUpdateRequest();
        request.setId("5a30c4f64e58db02286a3326");
        request.setProjectId(PROJECT_ID);
        request.setProjectTag(PROJECT_TAG);
        request.setName("leo");
        request.setMobile("13011112222");
        request.setEmail("l@timanetworks.com");
        //operator不存在
        mockProject();
        UserCacheDto dto = new UserCacheDto();
        dto.setRole(Role.SUPPER);
        PowerMockito.mockStatic(BizContext.class);
        when(BizContext.getData(anyString())).thenReturn(dto);
        when(operatorRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
        try {
            operatorService.update(request);
        } catch (BaseException e) {
            Assert.assertEquals("operator not exist", e.getReturnErrCode(), ErrEnum.DATA_NOT_EXIST.getCode());
        }

        //success
        Operator operator = mockOperator();
        when(operatorRepository.save(operator)).thenReturn(operator);

        mockReceivers(operator);
        operatorService.update(request);
        OperatorResponse response = new OperatorResponse();
        response.setData(operatorService.convertModelToDto(operator));
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getData());
    }

    public Operator mockOperator() {
        Operator operator = new Operator();
        operator.setName("zhangsan");
        operator.setEmail("l@timanetworks.com");
        operator.setMobile("13011111111");
//        operator.setProjectId(PROJECT_ID);
        when(operatorRepository.findById(anyString())).thenReturn(Optional.ofNullable(operator));
        return operator;
    }

    public void mockReceivers(Operator operator) {
        List<MonitorItem> items = new ArrayList<>();
        List<Operator> receivers = new ArrayList<>();
        Operator operator1 = new Operator();
        operator1.setId("5a30c4f64e58db02286a3326");
//        operator1.setProjectId(PROJECT_ID);
        operator1.setName("张三");
        operator1.setEmail("123@qq.com");
        operator1.setMobile("13045677654");
        receivers.add(operator1);

        MonitorItem item = new MonitorItem();
        item.setName("TEST");
        item.setProjectId("5a1fd7544e58db09149115af");
        item.setReceivers(receivers);
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
        items.add(item);
        when(monitorItemRepository.findByReceivers(operator)).thenReturn(items);
    }

    @Test
    public void test03Delete() throws BaseException {
        IdRequest request = new IdRequest();
        // id is null
        try {
            operatorService.delete(request);
        } catch (BaseException e) {
            Assert.assertEquals("operator id not be null", e.getReturnErrCode(), ErrEnum.OPERATOR_ID_NOT_BLANK.getCode());
        }
        // operator is null
        when(operatorRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
        try {
            operatorService.delete(request);
        } catch (BaseException e) {
            Assert.assertEquals("operator is not exist", e.getReturnErrCode(), ErrEnum.OPERATOR_ID_NOT_BLANK.getCode());
        }
        //project id is not equal
        request.setId("5a27bb2b4e58db6808b572fd");
        request.setProjectId(PROJECT_ID);
        request.setProjectTag("carnet");
        mockOperator();
        try {
            operatorService.delete(request);
        } catch (BaseException e) {
            Assert.assertEquals("project id is not equal", e.getReturnErrCode(), ErrEnum.DATA_NOT_EXIST.getCode());
        }

        //receiver is not empty
        request.setId("5a27bb2b4e58db6808b572fd");
        request.setProjectId(PROJECT_ID);
        request.setProjectTag(PROJECT_TAG);
        Operator operator = mockOperator();
        mockReceivers(operator);
        try {
            operatorService.delete(request);
        } catch (BaseException e) {
            Assert.assertEquals("monitoritem operator already bind", e.getReturnErrCode(), ErrEnum.MONITORITEM_OPERATOR_ALREADY_BIND.getCode());
        }

        //success
        request.setId("5a27bb2b4e58db6808b572fd");
        request.setProjectId(PROJECT_ID);
        request.setProjectTag(PROJECT_TAG);
        mockOperator();
        MonitorBaseResponse response = operatorService.delete(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getReturnSuccess());
    }

    @Test
    public void test04Page() {
        OperatorPageRequest request = new OperatorPageRequest();
        request.setCurPage(0);
        request.setPageSize(1);
        request.setProjectId(PROJECT_ID);
        request.setProjectTag(PROJECT_TAG);

        Page<Operator> page = new Page<Operator>() {
            @Override
            public int getTotalPages() {
                return 10;
            }

            @Override
            public long getTotalElements() {
                return 10;
            }

            @Override
            public <U> Page<U> map(Function<? super Operator, ? extends U> converter) {
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
            public List<Operator> getContent() {
                List<Operator> list = new ArrayList<>();
                Operator operator = new Operator();
                operator.setName("zhangsan");
                operator.setEmail("l@timanetworks.com");
                operator.setMobile("13011111111");
//                operator.setProjectId(PROJECT_ID);
                list.add(operator);
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
            public Iterator<Operator> iterator() {
                return null;
            }
        };
        mockProject();
        when(operatorRepository.advanceQuery(Arrays.asList(PROJECT_ID), PageUtil.convertPageRequestToPageable(request))).thenReturn(page);
        OperatorPageResponse response = operatorService.page(request);
        Assert.assertNotNull(response);
    }

    @Test
    public void test05List() {
        BmBaseRequest request = new BmBaseRequest();
        request.setProjectId(PROJECT_ID);
        request.setProjectTag(PROJECT_TAG);

        List<Operator> operators = new ArrayList<>();
        Operator operator = new Operator();
        operator.setName("zhangsan");
        operator.setEmail("l@timanetworks.com");
        operator.setMobile("13011111111");
//        operator.setProjectId(PROJECT_ID);
        operators.add(operator);
        mockProject();
//        when(operatorRepository.findByProjectId(request.getProjectId())).thenReturn(operators);
        OperatorListResponse response = operatorService.list(request);
        Assert.assertNotNull(response);
    }

    public void mockProject() {
        Project project = new Project();
        project.setPassword(PROJECT_PWD);
        project.setTag(PROJECT_TAG);
        project.setId(PROJECT_ID);

        when(projectRepository.findById(eq(PROJECT_ID))).thenReturn(Optional.ofNullable(project));
    }
}
