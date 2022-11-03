package com.carroll.biz.monitor.analyzer.service;/**
 * Created by core_ on 2017/11/22.
 */

import com.carroll.biz.monitor.analyzer.dto.WarnDataCallBackDto;
import com.carroll.biz.monitor.analyzer.model.WarningDataCallBack;
import com.carroll.biz.monitor.common.request.WarnDataCallBackRequest;
import com.carroll.biz.monitor.common.response.WarnDataCallBackResponse;
import com.carroll.spring.rest.starter.BaseException;

/**
 * 监控数据回调管理
 * Created on 2018/04/23 13:45 By tangbin
 * <p>
 * Copyright @ 2018 Tima Networks Inc. All Rights Reserved. 
 */
public interface IWarningDataCallBackService {


    /**
     * 新增监控数据回调
     *
     * @param request
     * @return
     */
    WarnDataCallBackResponse save(WarnDataCallBackRequest request) throws BaseException;

    WarningDataCallBack findByProjectTagAndEnable(String tag, boolean enable);

    WarnDataCallBackDto detail(String projectId) throws BaseException;

}
