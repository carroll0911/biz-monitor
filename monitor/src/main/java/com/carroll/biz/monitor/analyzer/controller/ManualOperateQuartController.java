package com.carroll.biz.monitor.analyzer.controller;

import com.carroll.biz.monitor.analyzer.service.IWarningDataService;
import com.carroll.spring.rest.starter.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 2017/12/14 10:33 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Api(value = "ManualOperateQuartController", description = "手动触发定时器")
@RestController
@RequestMapping("/interior/manual")
public class ManualOperateQuartController extends BaseController {

    @Autowired
    private IWarningDataService warningDataService;

    @ApiOperation(value = "手动触发清理僵尸数据定时器")
    @RequestMapping(value = "/clearUselessData", method = RequestMethod.GET)
    public void manualStartClearUselessDataJob() {
        warningDataService.clearUselessData();
    }

    @ApiOperation(value = "手动触发重新发送告警数据定时器")
    @RequestMapping(value = "/resendNotify", method = RequestMethod.GET)
    public void manualStartResendNotifyJob() {
        warningDataService.resendData();
    }
}
