package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.model.*;
import com.carroll.biz.monitor.analyzer.repository.*;
import com.carroll.biz.monitor.analyzer.service.impl.MonitorTargetServiceImpl;
import com.carroll.biz.monitor.analyzer.service.impl.WarningDataServiceImpl;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import com.carroll.biz.monitor.analyzer.utils.PageUtil;
import com.carroll.biz.monitor.analyzer.model.*;
import com.carroll.biz.monitor.analyzer.repository.*;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.request.CurrentMonitorRequest;
import com.carroll.biz.monitor.common.request.HistoryMonitorPageRequest;
import com.carroll.biz.monitor.common.request.IdRequest;
import com.carroll.biz.monitor.common.request.MonitorStatisticRequest;
import com.carroll.biz.monitor.common.response.CurrentMonitorResponse;
import com.carroll.biz.monitor.common.response.HistoryMonitorPageResponse;
import com.carroll.biz.monitor.common.response.MonitorDetailResponse;
import com.carroll.biz.monitor.common.response.MonitorStatisticResponse;
import com.carroll.cache.RedisUtil;
import com.carroll.spring.rest.starter.BaseException;
import com.carroll.utils.BeanUtils;
import com.carroll.utils.OldDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created on 2017/11/27 14:38 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@RunWith(PowerMockRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
@PrepareForTest({BizContext.class})
public class WarningDataServiceTest {
    @InjectMocks
    private WarningDataServiceImpl warningDataService;

    @Mock
    private MonitorTargetServiceImpl monitorTargetService;

    @Mock
    private MonitorItemRepository monitorItemRepository;

    @Mock
    private WarningDataRepository warningDataRepository;
    @Mock
    private WarningDataHistoryRepository warningDataHistoryRepository;

    @Mock
    private WarningDataMongoDao warningDataMongoDao;

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private KafkaTemplate kafkaTemplate;
    @Mock
    private RedisUtil redisUtil;

    @Test
    public void test01GetCurrentData() {
        List<WarningData> list = new ArrayList<>();
        WarningData warningData = new WarningData();
        warningData.setHost("127.0.0.1");
        warningData.setId("1");
        warningData.setItemId("1");
        warningData.setStatus(WarningData.Status.NORMAL);
        list.add(warningData);
        when(redisUtil.set(anyString(),anyObject(),anyLong())).thenReturn(Boolean.TRUE);
        when(redisUtil.get(anyString())).thenReturn(warningData);
        when(warningDataRepository.findByItemIdAndApplicationNameAndStatusAndHost("1", "c3", WarningData.Status.NORMAL, "127.0.0.1")).thenReturn(list);
        when(warningDataRepository.findWarnData("1", "c3", WarningData.Status.NORMAL, "127.0.0.1", null)).thenReturn(list);
        WarningData result = warningDataService.getCurrentData("1", "c3", "127.0.0.1", null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getHost().equals("127.0.0.1"));

    }

    @Test
    public void test02Save() {
        WarningData warningData = new WarningData();
        warningData.setHost("127.0.0.1");
        warningData.setId("1");
        warningData.setStatus(WarningData.Status.NORMAL);
        warningData.setCycleTimes(10L);
        warningData.setLatestTime(new Date());
        warningData.setSuccessTimes(0L);
        warningData.setApplicationName("c3");
        warningData.setItemId("2");
        when(warningDataRepository.save(warningData)).thenReturn(warningData);
        warningDataService.save(warningData);
    }

    @Test
    public void test03ClearUselessData() throws NoSuchFieldException, IllegalAccessException {
        mockWarningDataServiceField();
        when(kafkaTemplate.send(anyString(), anyString())).thenReturn(null);
        when(warningDataMongoDao.getPageCount(anyObject())).thenReturn(1L);
        mockWarningDataList();
        MonitorItem item = initMonitorItem();
        when(monitorItemRepository.findById("2")).thenReturn(Optional.ofNullable((item)));
        warningDataService.clearUselessData();
    }

    @Test
    public void test04ResendData() throws NoSuchFieldException, IllegalAccessException {
        mockWarningDataServiceField();
        when(warningDataMongoDao.getPageCount(anyObject())).thenReturn(1L);
        mockWarningDataList();
        MonitorItem item = initMonitorItem();
        when(monitorItemRepository.findById("2")).thenReturn(Optional.ofNullable(item));
        warningDataService.resendData();
    }

    private void mockWarningDataServiceField() throws NoSuchFieldException, IllegalAccessException {
        Field daysField = WarningDataServiceImpl.class.getDeclaredField("days");
        daysField.setAccessible(true);
        daysField.set(warningDataService, new Integer(3));
        Field numField = WarningDataServiceImpl.class.getDeclaredField("num");
        numField.setAccessible(true);
        numField.set(warningDataService, new Integer(3));
    }

    private void mockUserCache(){
        UserCacheDto dto=new UserCacheDto();
        dto.setRole(Role.SUPPER);
        PowerMockito.mockStatic(BizContext.class);
        when(BizContext.getData(anyString())).thenReturn(dto);
    }

    @Test
    public void test05MonitorDetail() throws BaseException {
        IdRequest request = new IdRequest();
        MonitorDetailResponse response = null;

        mockUserCache();
        //warningdata_not_exist
        request.setId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        request.setId("1");
        mockProject();
        mockWarningData();
        MonitorItem item = initMonitorItem();
        when(warningDataRepository.findById(anyString())).thenReturn(Optional.ofNullable(null));
        when(monitorItemRepository.findById(anyString())).thenReturn(Optional.ofNullable(item));
        try {
            response = warningDataService.monitorDetail(request);
            Assert.assertNotNull(response);
            Assert.assertTrue(response.getReturnSuccess());
            Assert.assertNotNull(response.getData());
        } catch (BaseException e) {
            Assert.assertEquals("id不存在", e.getReturnErrCode(), ErrEnum.WARNINGDATA_NOT_EXIST.getCode());
        }

        //warningdata_project_not_match
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        mockProject();
        mockWarningData();
        when(monitorItemRepository.findByProjectIdAndId(anyString(), anyString())).thenReturn(null);
        try {
            warningDataService.monitorDetail(request);
        } catch (BaseException e) {
            Assert.assertEquals("warningdata_project_not_match", e.getReturnErrCode(), ErrEnum.WARNINGDATA_PROJECT_NOT_MATCH.getCode());
        }
        //success
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        mockProject();
        mockWarningData();
        item = initMonitorItem();
        when(monitorItemRepository.findById(anyString())).thenReturn(Optional.ofNullable(item));
        response = warningDataService.monitorDetail(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getReturnSuccess());
    }

    @Test
    public void test06HistoryMonitor() throws BaseException {
        HistoryMonitorPageRequest request = new HistoryMonitorPageRequest();
        request.setCurPage(0);
        request.setPageSize(10);
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        mockProject();
        List<String> itemIds = new ArrayList<>();
        itemIds.add("1");
        itemIds.add("2");
        when(warningDataService.verifyPorjectAndGetMonitorItemIds(request.getProjectId(),
                request.getProjectTag(), request.getItemId(), request.getLevel())).thenReturn(itemIds);
        List<MonitorItem> list = initMonitorItemList();
        when(monitorItemRepository.advanceQuery(anyList(), anyString(), anyString())).thenReturn(list);
        Page<WarningDataHistory> page = mockWarningDataHistoryPageList();
        when(warningDataHistoryRepository.advanceQuery(eq(request.getFirstStartTime()),
                eq(request.getFirstEndTime()),
                eq(request.getRecoveryStartTime()),
                eq(request.getRecoveryEndTime()),
                eq(itemIds), eq(request.getApplicationName()), eq(request.getTarget()),anyList(), anyObject())).thenReturn(page);
        MonitorItem item = initMonitorItem();
        when(monitorItemRepository.findById("2")).thenReturn(Optional.ofNullable(item));
        HistoryMonitorPageResponse response = warningDataService.historyMonitor(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getReturnSuccess());

    }

    @Test
    public void test07CurrentMonitor() throws BaseException {
        CurrentMonitorRequest request = new CurrentMonitorRequest();
        request.setCurPage(0);
        request.setPageSize(10);
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        request.setApplicationName("UT");
        request.setFirstStartTime(new Date());
        request.setFirstEndTime(new Date());
        request.setUpdateStartTime(new Date());
        request.setUpdateEndTime(new Date());
        request.setItemId("1");
        request.setLevel(MonitorItem.Level.CRITICAL.name());
        mockProject();
        List<String> itemIds = new ArrayList<>();
        itemIds.add("1");
        itemIds.add("2");
        when(warningDataService.verifyPorjectAndGetMonitorItemIds(request.getProjectId(),
                request.getProjectTag(), request.getItemId(), request.getLevel())).thenReturn(itemIds);
        Page<WarningData> page = mockWarningDataPageList();
        when(warningDataRepository.advanceQueryByPage(request.getFirstStartTime(),
                request.getFirstEndTime(), request.getUpdateStartTime(), request.getUpdateEndTime(),
                itemIds, request.getApplicationName(), "LMGAC1G82H1250921",null, PageUtil.convertPageRequestToPageable(request))).thenReturn(page);
        MonitorItem item = initMonitorItem();
        when(monitorItemRepository.findById("2")).thenReturn(Optional.ofNullable(item));
        List<MonitorItem> list = initMonitorItemList();
        when(monitorItemRepository.advanceQuery(anyList(), anyString(), anyString())).thenReturn(list);
        CurrentMonitorResponse response = warningDataService.currentMonitor(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getReturnSuccess());
    }

    @Test
    public void test08Statistic() throws Exception {
        MonitorStatisticResponse response;
        MonitorStatisticRequest request = new MonitorStatisticRequest();
        //project not exist
        request.setProjectId("");
        when(projectRepository.findById(anyString())).thenReturn(Optional.empty());
        try {
            warningDataService.statistic(request);
        } catch (BaseException e) {
            Assert.assertEquals("project not exist", e.getReturnErrCode(), ErrEnum.DATA_NOT_EXIST.getCode());
        }

        //project tag not equal
        request.setProjectTag("c1");
        mockProject();
        try {
            warningDataService.statistic(request);
        } catch (BaseException e) {
            Assert.assertEquals("project not exist", e.getReturnErrCode(), ErrEnum.DATA_NOT_EXIST.getCode());
        }

        //type不传时，查询normal状态的数据总条数, warningdata_status_not_null
        request.setProjectTag("c3");
        request.setProjectId("5a163b834668c04574f962ea");
        mockProject();
        mockUserCache();
        List<MonitorItem> list = initMonitorItemList();
        when(monitorItemRepository.findByProjectId(anyString())).thenReturn(list);
        try {
            warningDataService.statistic(request);
        } catch (BaseException e) {
            Assert.assertEquals("project not exist", e.getReturnErrCode(), ErrEnum.WARNINGDATA_STATUS_NOT_NULL.getCode());
        }

        //type不传时，查询normal状态的数据总条数, success
        request.setProjectTag("c3");
        request.setProjectId("5a163b834668c04574f962ea");
        request.setStatus(WarningData.Status.NORMAL.name());
        mockProject();
        List<MonitorItem> itemList = initMonitorItemList();
        when(monitorItemRepository.findByProjectId(anyString())).thenReturn(itemList);
        List<String> itemIds = new ArrayList<>();
        itemIds.add("1");
        when(warningDataRepository.currentStatistic(null, null, null,
                null, null, null, null, request.getStatus(), Arrays.asList("5a163b834668c04574f962ea"))).thenReturn(10);
        response = warningDataService.statistic(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getList());
        Assert.assertTrue(response.getList().get(0).get(0).getTimes() == 10);


        //statistic by source
        request.setProjectId("5a163b834668c04574f962ea");
        request.setProjectTag("c3");
        request.setStatus("ALL");
        request.setType("LEVEL");
        mockProject();
        itemList = initMonitorItemList();
        when(monitorItemRepository.findByProjectId(anyString())).thenReturn(itemList);
        itemIds = new ArrayList<>();
        itemIds.add("1");
        when(mongoTemplate.aggregate(any(TypedAggregation.class), eq("warningData"), eq(WarningData.class))).thenReturn(new AggregationResults(new ArrayList(), new Document()));
        when(mongoTemplate.aggregate(any(TypedAggregation.class), eq("warningDataHistory"), eq(WarningDataHistory.class))).thenReturn(new AggregationResults(new ArrayList(), new Document()));
        response = warningDataService.statistic(request);
        Assert.assertNotNull(response.getList());

        //statistic by name
        request.setStatus("ALL");
        request.setType("SOURCE");
        mockProject();
        itemList = initMonitorItemList();
        when(monitorItemRepository.findByProjectId(anyString())).thenReturn(itemList);
        itemIds = new ArrayList<>();
        itemIds.add("1");

        response = warningDataService.statistic(request);
        Assert.assertNotNull(response.getList());

        //statistic by level
        request.setStatus("ALL");
        request.setType("NAME");
        mockProject();
        itemList = initMonitorItemList();
        when(monitorItemRepository.findByProjectId(anyString())).thenReturn(itemList);
        itemIds = new ArrayList<>();
        itemIds.add("1");
        response = warningDataService.statistic(request);
        Assert.assertNotNull(response.getList());
    }

    private void mockProject() {
        Project project = new Project();
        project.setTag("c3");
        project.setId("5a163b834668c04574f962ea");
        when(projectRepository.findById(anyString())).thenReturn(Optional.ofNullable(project));
    }

    private void mockWarningData() {
        WarningData data = new WarningData();
        data.setId("1");
        data.setItemId("1");
        data.setApplicationName("UT test");
        data.setHost("127.0.0.1");
        data.setStatus(WarningData.Status.NORMAL);
        data.setTimes(10L);
        data.setFirstTime(new Date());
        data.setLastSendTime(new Date());
        data.setSuccessTimes(1L);
        data.setRecoveryTime(new Date());
        List<LogData> logs = new ArrayList<>();
        LogData log = new LogData();
        log.setParams("UT test");
        log.setResponse("UT test");
        logs.add(log);
        data.setLogs(logs);
        data.setProjectId("5a163b834668c04574f962ea");
        WarningDataHistory history = new WarningDataHistory();
        BeanUtils.copyPropertiesIgnorException(data,history);
        when(warningDataRepository.findById(anyString())).thenReturn(Optional.ofNullable(data));
        when(warningDataHistoryRepository.findById(anyString())).thenReturn(Optional.ofNullable(history));
    }

    private void mockWarningDataList() {
        List<WarningData> list = new ArrayList<>();
        WarningData warningData = new WarningData();
        warningData.setHost("127.0.0.1");
        warningData.setId("1");
        warningData.setStatus(WarningData.Status.NORMAL);
        warningData.setCycleTimes(10L);
        warningData.setLatestTime(new Date());
        warningData.setSuccessTimes(0L);
        warningData.setApplicationName("c3");
        warningData.setItemId("2");
        String date = "2017-12-19 00:00:00";

        try {
            warningData.setFirstTime(OldDateUtils.parse(date));
            warningData.setLastSendTime(OldDateUtils.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list.add(warningData);
        when(warningDataMongoDao.getPage(anyObject(), anyObject())).thenReturn(list);
        when(warningDataRepository.save(warningData)).thenReturn(warningData);
    }

    private MonitorItem initMonitorItem() {
        MonitorItem monitorItem = new MonitorItem();
        monitorItem.setId("1");
        monitorItem.setSendFlag(true);
        monitorItem.setName("UT test");
        monitorItem.setTimes(10L);
        monitorItem.setRecoveryTimes(10L);
        monitorItem.setSuggest("UT test");
        monitorItem.setDescription("UT test");
        List<MonitorItem.MessageType> msgType = new ArrayList<>();
        msgType.add(MonitorItem.MessageType.EMAIL);
        monitorItem.setMsgTypes(msgType);
        monitorItem.setTag("c3");
        monitorItem.setCycleTimes(5L);
        monitorItem.setLevel(MonitorItem.Level.CRITICAL);
        monitorItem.setCategory(MonitorItem.Category.BUSINESS);
        monitorItem.setProjectId("5a163b834668c04574f962ea");
        List<Operator> receivers = new ArrayList<>();
        Operator operator = new Operator();
        operator.setName("UT test");
        operator.setEmail("li.lan@timanetworks.com");
        operator.setMobile("13818816319");
        receivers.add(operator);
        monitorItem.setReceivers(receivers);
        return monitorItem;
    }


    private List<MonitorItem> initMonitorItemList() {
        List<MonitorItem> list = new ArrayList<>();
        MonitorItem monitorItem = new MonitorItem();
        monitorItem.setId("1");
        monitorItem.setSendFlag(true);
        monitorItem.setName("UT test");
        monitorItem.setTimes(10L);
        monitorItem.setRecoveryTimes(10L);
        monitorItem.setSuggest("UT test");
        monitorItem.setDescription("UT test");
        List<MonitorItem.MessageType> msgType = new ArrayList<>();
        msgType.add(MonitorItem.MessageType.EMAIL);
        monitorItem.setMsgTypes(msgType);
        monitorItem.setTag("c3");
        monitorItem.setCycleTimes(5L);
        monitorItem.setLevel(MonitorItem.Level.CRITICAL);
        monitorItem.setCategory(MonitorItem.Category.BUSINESS);
        monitorItem.setProjectId("5a163b834668c04574f962ea");
        List<Operator> receivers = new ArrayList<>();
        Operator operator = new Operator();
        operator.setName("UT test");
        operator.setEmail("li.lan@timanetworks.com");
        operator.setMobile("13818816319");
        receivers.add(operator);
        monitorItem.setReceivers(receivers);
        list.add(monitorItem);
        return list;
    }

    private Page<WarningData> mockWarningDataPageList() {
        Page<WarningData> page = new Page<WarningData>() {
            @Override
            public int getTotalPages() {
                return 10;
            }

            @Override
            public long getTotalElements() {
                return 10;
            }

            @Override
            public <U> Page<U> map(Function<? super WarningData, ? extends U> converter) {
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
                return 10;
            }

            @Override
            public List<WarningData> getContent() {
                List<WarningData> list = new ArrayList<>();
                WarningData data = new WarningData();
                data.setId("1");
                data.setItemId("1");
                data.setApplicationName("UT test");
                data.setHost("127.0.0.1");
                data.setStatus(WarningData.Status.NORMAL);
                data.setTimes(10L);
                data.setFirstTime(new Date());
                data.setLastSendTime(new Date());
                data.setSuccessTimes(1L);
                data.setRecoveryTime(new Date());
                list.add(data);
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
            public Iterator<WarningData> iterator() {
                return null;
            }
        };
        return page;
    }

    private Page<WarningDataHistory> mockWarningDataHistoryPageList() {
        Page<WarningDataHistory> page = new Page<WarningDataHistory>() {
            @Override
            public int getTotalPages() {
                return 10;
            }

            @Override
            public long getTotalElements() {
                return 10;
            }

            @Override
            public <U> Page<U> map(Function<? super WarningDataHistory, ? extends U> converter) {
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
                return 10;
            }

            @Override
            public List<WarningDataHistory> getContent() {
                List<WarningDataHistory> list = new ArrayList<>();
                WarningDataHistory data = new WarningDataHistory();
                data.setId("1");
                data.setItemId("1");
                data.setApplicationName("UT test");
                data.setHost("127.0.0.1");
                data.setStatus(WarningData.Status.NORMAL);
                data.setTimes(10L);
                data.setFirstTime(new Date());
                data.setLastSendTime(new Date());
                data.setSuccessTimes(1L);
                data.setRecoveryTime(new Date());
                list.add(data);
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
            public Iterator<WarningDataHistory> iterator() {
                return null;
            }
        };
        return page;
    }


}
