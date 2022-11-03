package com.carroll.biz.monitor.analyzer.controller;

import com.carroll.biz.monitor.common.request.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.carroll.biz.monitor.analyzer.service.IOperatorService;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.OperatorListResponse;
import com.carroll.biz.monitor.common.response.OperatorPageResponse;
import com.carroll.biz.monitor.common.response.OperatorResponse;
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

/**
 * Created on 2017/12/5 13:53 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Api(value = "OperatorController",description = "运营人员管理")
@RestController
@RequestMapping("/operator")
public class OperatorController extends BaseController {

    @Autowired
    private IOperatorService operatorService;

    @ApiOperation(value = "新增运营人员")
    @RequestMapping(method = RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "saveFallBack", ignoreExceptions = {BaseException.class})
    public OperatorResponse save(@RequestBody @Valid final OperatorRequest request, BindingResult result) throws BaseException {
        return operatorService.save(request);
    }

    @SuppressWarnings("unused")
    public OperatorResponse saveFallBack(OperatorRequest request, BindingResult result, Throwable throwable) {
        OperatorResponse response = new OperatorResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    @ApiOperation(value = "编辑运营人员")
    @RequestMapping(method = RequestMethod.PUT)
    @HystrixCommand(fallbackMethod = "updateFallBack", ignoreExceptions = {BaseException.class})
    public OperatorResponse update(@Valid @RequestBody OperatorUpdateRequest request, BindingResult result) throws BaseException {
        return operatorService.update(request);
    }

    @SuppressWarnings("unused")
    public OperatorResponse updateFallBack(OperatorUpdateRequest request, BindingResult result, Throwable throwable) {
        OperatorResponse response = new OperatorResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    @ApiOperation(value = "删除运营人员")
    @RequestMapping(method = RequestMethod.DELETE)
    @HystrixCommand(fallbackMethod = "deleteFallBack", ignoreExceptions = {BaseException.class})
    public MonitorBaseResponse delete(@Valid @ModelAttribute IdRequest request, BindingResult result) throws BaseException {
        return operatorService.delete(request);
    }

    @SuppressWarnings("unused")
    public MonitorBaseResponse deleteFallBack(IdRequest request, BindingResult result, Throwable throwable) {
        return fallBackResponse(throwable);
    }

    @ApiOperation(value = "分页查询运营人员")
    @RequestMapping(method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "pageFallBack", ignoreExceptions = {BaseException.class})
    public OperatorPageResponse page(@Valid @ModelAttribute OperatorPageRequest request, BindingResult result) {
        return operatorService.page(request);
    }

    @SuppressWarnings("unused")
    public OperatorPageResponse pageFallBack(OperatorPageRequest request, BindingResult result, Throwable throwable) {
        OperatorPageResponse response = new OperatorPageResponse();
        BeanUtils.copyPropertiesIgnorException(fallBackResponse(throwable), response);
        return response;
    }

    @ApiOperation(value = "列表查询运营人员")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "listFallBack", ignoreExceptions = {BaseException.class})
    public OperatorListResponse list(@Valid @ModelAttribute BmBaseRequest request, BindingResult result) {
        return operatorService.list(request);
    }

    @SuppressWarnings("unused")
    public OperatorListResponse listFallBack(BmBaseRequest request, BindingResult result, Throwable throwable) {
        OperatorListResponse response = new OperatorListResponse();
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
