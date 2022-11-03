package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.response.LoginResponse;
import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.request.LoginRequest;
import com.carroll.biz.monitor.analyzer.request.ModifyPasswordReq;
import com.carroll.biz.monitor.common.request.*;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.OperatorListResponse;
import com.carroll.biz.monitor.common.response.OperatorPageResponse;
import com.carroll.biz.monitor.common.response.OperatorResponse;
import com.carroll.spring.rest.starter.BaseException;

/**
 * Created on 2017/12/5 15:14 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface IOperatorService {

    OperatorResponse save(OperatorRequest request) throws BaseException;

    OperatorResponse update(OperatorUpdateRequest request) throws BaseException;

    MonitorBaseResponse delete(IdRequest request) throws BaseException;

    OperatorPageResponse page(OperatorPageRequest request);

    OperatorListResponse list(BmBaseRequest request);

    /**
     * 登陆
     * @param request
     * @return
     */
    LoginResponse login(LoginRequest request) throws MonitorBaseException;

    /**
     * 注销登录
     * @return
     */
    MonitorBaseResponse logout();

    UserCacheDto getUserCache(String userId);

    /**
     * 修改密码
     * @param req
     */
    void modifyPwd(ModifyPasswordReq req) throws MonitorBaseException;

    /**
     * 重置密码
     * @param userId
     */
    void resetPwd(String userId) throws MonitorBaseException;

    /**
     * 修改用户信息
     * @param email
     * @param mobile
     */
    void updateMyInfo(String email,String mobile) throws BaseException;
}
