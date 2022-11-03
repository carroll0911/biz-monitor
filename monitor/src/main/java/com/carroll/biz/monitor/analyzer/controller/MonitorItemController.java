package com.carroll.biz.monitor.analyzer.controller;

import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.service.IMonitorItemService;
import com.carroll.biz.monitor.common.dto.ConstantDto;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.request.*;
import com.carroll.biz.monitor.common.response.*;
import com.carroll.spring.rest.starter.BaseController;
import com.carroll.spring.rest.starter.BaseException;
import com.carroll.utils.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/12/5 16:21 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Api(value = "MonitorItemController", description = "告警内容管理")
@RestController
@RequestMapping("/item")
public class MonitorItemController extends BaseController {

    @Autowired
    private IMonitorItemService monitorItemService;

    @ApiOperation(value = "查询告警内容id和name")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "listFallBack", ignoreExceptions = {BaseException.class})
    public MonitorItemListResponse list(@Valid @ModelAttribute BmBaseRequest request, BindingResult result) {
        return monitorItemService.list(request);
    }

    @SuppressWarnings("unused")
    public MonitorItemListResponse listFallBack(BmBaseRequest request, BindingResult result, Throwable throwable) {
        MonitorItemListResponse response = new MonitorItemListResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    @ApiOperation(value = "查询告警内容详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "detailFallBack", ignoreExceptions = {BaseException.class})
    public MonitorItemResponse detail(@Valid @ModelAttribute IdRequest request, BindingResult result) throws BaseException {
        return monitorItemService.detail(request);
    }

    @SuppressWarnings("unused")
    public MonitorItemResponse detailFallBack(IdRequest request, BindingResult result, Throwable throwable) {
        MonitorItemResponse response = new MonitorItemResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    @ApiOperation(value = "分页查询告警内容")
    @RequestMapping(method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "pageFallBack", ignoreExceptions = {BaseException.class})
    public MonitorItemPageResponse page(@Valid @ModelAttribute MonitorItemPageRequest request, BindingResult result) {
        return monitorItemService.page(request);
    }

    @SuppressWarnings("unused")
    public MonitorItemPageResponse pageFallBack(MonitorItemPageRequest request, BindingResult result, Throwable throwable) {
        MonitorItemPageResponse response = new MonitorItemPageResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    @ApiOperation(value = "编辑告警内容")
    @RequestMapping(method = RequestMethod.PUT)
//    @HystrixCommand(fallbackMethod = "updateFallBack", ignoreExceptions = {BaseException.class})
    public MonitorItemResponse update(@Valid @RequestBody MonitorItemUpdateRequest request, BindingResult result) throws BaseException {
        return monitorItemService.update(request);
    }

    @SuppressWarnings("unused")
    public MonitorItemResponse updateFallBack(MonitorItemUpdateRequest request, BindingResult result, Throwable throwable) {
        MonitorItemResponse response = new MonitorItemResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    @ApiOperation(value = "通知设置")
    @RequestMapping(value = "/notify", method = RequestMethod.PUT)
//    @HystrixCommand(fallbackMethod = "notifyFallBack", ignoreExceptions = {BaseException.class})
    public MonitorBaseResponse notify(@Valid @RequestBody MonitorItemNotifyRequest request, BindingResult result) throws BaseException {
       return monitorItemService.notify(request);
    }

    @SuppressWarnings("unused")
    public MonitorBaseResponse notifyFallBack(MonitorItemNotifyRequest request, BindingResult result, Throwable throwable) {
        return fallBackResponse(throwable);
    }

    @ApiOperation(value = "获取告警级别")
    @RequestMapping(value = "/level", method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "loadConstantFallBack", ignoreExceptions = {BaseException.class})
    public MonitorItemConstantResponse loadLevel() {
        MonitorItemConstantResponse response = new MonitorItemConstantResponse();
        List<ConstantDto> list = new ArrayList<>();
        for(MonitorItem.Level level: MonitorItem.Level.values()) {
            ConstantDto dto = new ConstantDto();
            dto.setCode(level.name());
            dto.setDesc(level.getDesc());
            list.add(dto);
        }
        response.setList(list);
        return response;
    }

    @ApiOperation(value = "获取告警类别")
    @RequestMapping(value = "/category", method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "loadConstantFallBack", ignoreExceptions = {BaseException.class})
    public MonitorItemConstantResponse loadCategory() {
        MonitorItemConstantResponse response = new MonitorItemConstantResponse();
        List<ConstantDto> list = new ArrayList<>();
        for(MonitorItem.Category category: MonitorItem.Category.values()) {
            ConstantDto dto = new ConstantDto();
            dto.setCode(category.name());
            dto.setDesc(category.getDesc());
            list.add(dto);
        }
        response.setList(list);
        return response;
    }

    @ApiOperation(value = "获取告警通知类型")
    @RequestMapping(value = "/message", method = RequestMethod.GET)
//    @HystrixCommand(fallbackMethod = "loadConstantFallBack", ignoreExceptions = {BaseException.class})
    public MonitorItemConstantResponse loadMessage() {
        MonitorItemConstantResponse response = new MonitorItemConstantResponse();
        List<ConstantDto> list = new ArrayList<>();
        for(MonitorItem.MessageType msg: MonitorItem.MessageType.values()) {
            ConstantDto dto = new ConstantDto();
            dto.setCode(msg.name());
            dto.setDesc(msg.getDesc());
            list.add(dto);
        }
        response.setList(list);
        return response;
    }

    @ApiOperation(value = "新增告警内容,仅供内部使用")
    @RequestMapping(method = RequestMethod.POST)
    public MonitorItemResponse save(@Valid @RequestBody MonitorItemRequest request, BindingResult result) throws BaseException {
        return monitorItemService.save(request);
    }

    @SuppressWarnings("unused")
    public MonitorItemConstantResponse loadConstantFallBack(Throwable throwable) {
        MonitorItemConstantResponse response = new MonitorItemConstantResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    private MonitorBaseResponse fallBackResponse(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        MonitorBaseResponse response = new MonitorBaseResponse();
        response.setReturnSuccess(false);
        response.setReturnErrMsg(ErrEnum.SERVICE_UNAVAILABLE.getMsg());
        response.setReturnErrCode(ErrEnum.SERVICE_UNAVAILABLE.getCode());
        return response;
    }
}
