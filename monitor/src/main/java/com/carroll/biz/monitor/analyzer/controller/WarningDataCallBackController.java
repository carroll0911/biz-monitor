package com.carroll.biz.monitor.analyzer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.carroll.biz.monitor.analyzer.dto.WarnDataCallBackDto;
import com.carroll.biz.monitor.analyzer.response.WarnDataCallbacktDetailResponse;
import com.carroll.biz.monitor.analyzer.service.IWarningDataCallBackService;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.request.WarnDataCallBackRequest;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.WarnDataCallBackResponse;
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
 * Created on 2017/11/30 9:46 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Api(value = "WarningDataCallBackController", description = "监控数据回调")
@RestController
@RequestMapping("/warningDataCallBack")
public class WarningDataCallBackController extends BaseController {

    @Autowired
    private IWarningDataCallBackService warningDataService;

    @ApiOperation(value = "设置监控数据推送", notes = "相同tag覆盖")
    @RequestMapping(method = RequestMethod.POST)
    @HystrixCommand(fallbackMethod = "saveFallBack", ignoreExceptions = {BaseException.class})
    public WarnDataCallBackResponse save(@Valid @RequestBody WarnDataCallBackRequest request, BindingResult result) throws BaseException {
        return warningDataService.save(request);
    }

    @ApiOperation(value = "获取监控数据推送")
    @RequestMapping(method = RequestMethod.GET)
    public WarnDataCallbacktDetailResponse detail(@RequestParam("projectId") String projectId) throws BaseException {
        WarnDataCallBackDto dto = warningDataService.detail(projectId);
        WarnDataCallbacktDetailResponse response = new WarnDataCallbacktDetailResponse();
        response.setData(dto);
        return response;
    }

    @SuppressWarnings("unused")
    public WarnDataCallBackResponse saveFallBack(WarnDataCallBackRequest request, BindingResult result, Throwable throwable) {
        WarnDataCallBackResponse response = new WarnDataCallBackResponse();
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
