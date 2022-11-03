package com.carroll.biz.monitor.analyzer.service.impl;

import com.carroll.biz.monitor.analyzer.enums.ErrEnum;
import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.repository.MonitorItemRepository;
import com.carroll.biz.monitor.analyzer.repository.OperatorRepository;
import com.carroll.biz.monitor.analyzer.repository.UserProjectRepository;
import com.carroll.biz.monitor.analyzer.response.LoginResponse;
import com.carroll.biz.monitor.analyzer.service.IOperatorService;
import com.carroll.biz.monitor.analyzer.auth.TokenMessage;
import com.carroll.biz.monitor.analyzer.config.PassportConf;
import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.Operator;
import com.carroll.biz.monitor.analyzer.model.UserProject;
import com.carroll.biz.monitor.analyzer.request.LoginRequest;
import com.carroll.biz.monitor.analyzer.request.ModifyPasswordReq;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import com.carroll.biz.monitor.analyzer.utils.EmailUtils;
import com.carroll.biz.monitor.analyzer.utils.PageUtil;
import com.carroll.biz.monitor.analyzer.utils.TokenUtils;
import com.carroll.biz.monitor.common.dto.OperatorDto;
import com.carroll.biz.monitor.common.request.*;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.OperatorListResponse;
import com.carroll.biz.monitor.common.response.OperatorPageResponse;
import com.carroll.biz.monitor.common.response.OperatorResponse;
import com.carroll.cache.RedisUtil;
import com.carroll.spring.rest.starter.BaseException;
import com.carroll.utils.BeanUtils;
import com.carroll.utils.Md5Util;
import com.carroll.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.carroll.biz.monitor.common.enums.ErrEnum.*;

/**
 * Created on 2017/12/5 15:14 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Service
@Slf4j
public class OperatorServiceImpl extends BaseServiceImpl implements IOperatorService {
    private static final String REDIS_TOKEN_KEY = "_portal_token";

    private static final String USER_CACHE_KEY = "user_info_";

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private MonitorItemRepository monitorItemRepository;
    @Autowired
    private UserProjectRepository userProjectRepository;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private PassportConf passportConf;

    @Value("${webUrl:}")
    private String webUrl;

    @Override
    public OperatorResponse save(OperatorRequest request) throws BaseException {
        checkPermission();
        OperatorResponse response = new OperatorResponse();
        verifyEmailAndMobile(null, request.getEmail(), request.getMobile());
        Operator operator = convertRequestToModel(request);
        String pwd = StringUtil.generatePwd();
        operator.setPassword(Md5Util.md5Encode(pwd));
        operatorRepository.save(operator);
        response.setData(convertModelToDto(operator));
        emailUtils.sendEmail(new String[]{operator.getEmail()}, "【监控平台】账号变更通知",
                String.format("监控平台管理员已为您创建账户：%s，登录初始密码为：%s，请尽快登录修改密码;主页地址:%s", operator.getEmail(), pwd, webUrl));
        return response;
    }

    @Override
    @CacheEvict(value = {"monitorItem", "monitorItem#2*60*60", "monitorItem#list#byProject#2*60*60",
            "monitorItem#list#byLevel#2*60*60", "monitorItem#list#ByReceiver#2*60*60"}, allEntries = true)
    public OperatorResponse update(OperatorUpdateRequest request) throws BaseException {
        checkPermission();
        OperatorResponse response = new OperatorResponse();
        Operator operator = operatorRepository.findById(request.getId()).orElse(null);
        if (operator == null) {
            throw new BaseException(com.carroll.biz.monitor.common.enums.ErrEnum.DATA_NOT_EXIST);
        }
        verifyEmailAndMobile(request.getId(), request.getEmail(), request.getMobile());
        List<MonitorItem> items = monitorItemRepository.findByReceivers(operator);
        Date now = new Date();
        if (!CollectionUtils.isEmpty(items)) {
            items.forEach(item -> {
                item.getReceivers().forEach(receiver -> {
                    if (receiver.getId().equals(request.getId())) {
                        BeanUtils.copyPropertiesIgnorException(request, receiver);
                    }
                    receiver.setUpdateTime(now);
                    monitorItemRepository.save(item);
                });
            });
        }
        BeanUtils.copyPropertiesIgnorException(request, operator);
        operator.setUpdateTime(now);
        operatorRepository.save(operator);
        response.setData(convertModelToDto(operator));
        return response;
    }

    @Override
    @CacheEvict(value = {"monitorItem", "monitorItem#2*60*60", "monitorItem#list#byProject#2*60*60",
            "monitorItem#list#byLevel#2*60*60", "monitorItem#list#ByReceiver#2*60*60"}, allEntries = true)
    public MonitorBaseResponse delete(IdRequest request) throws BaseException {
        if (StringUtils.isEmpty(request.getId())) {
            throw new BaseException(OPERATOR_ID_NOT_BLANK.getCode(), OPERATOR_ID_NOT_BLANK.getMsg());
        }
        Operator operator = operatorRepository.findById(request.getId()).orElse(null);
        if (null == operator) {
            throw new BaseException(com.carroll.biz.monitor.common.enums.ErrEnum.DATA_NOT_EXIST);
        }
        List<MonitorItem> items = monitorItemRepository.findByReceivers(operator);
        if (!CollectionUtils.isEmpty(items)) {
            throw new BaseException(MONITORITEM_OPERATOR_ALREADY_BIND.getCode(), MONITORITEM_OPERATOR_ALREADY_BIND.getMsg());
        }
        operatorRepository.delete(operator);
        return new MonitorBaseResponse();
    }

    @Override
    public OperatorPageResponse page(OperatorPageRequest request) {
        OperatorPageResponse response = new OperatorPageResponse();
        List<OperatorDto> list = new ArrayList<>();
        response.setList(list);
        List<String> projects = getCurrentUserProjects(request.getProjectId());
        if (projects == null) {
            return response;
        }
        Page<Operator> page = operatorRepository.advanceQuery(projects, PageUtil.convertPageRequestToPageable(request));
        if (page != null) {
            response = PageUtil.convertPageToPageResponse(page, response);
            page.getContent().forEach(operator -> {
                list.add(convertModelToDto(operator));
            });
        }
        response.setList(list);
        return response;
    }

    @Override
    public OperatorListResponse list(BmBaseRequest request) {
        OperatorListResponse response = new OperatorListResponse();
        List<String> projects = getCurrentUserProjects(request.getProjectId());
        if (projects == null) {
            return response;
        }
        List<Operator> list;
        if (CollectionUtils.isEmpty(projects)) {
            list = operatorRepository.findAll();
        } else {
            List<UserProject> ups = userProjectRepository.findAllByProjectIdIsIn(projects);
            if (CollectionUtils.isEmpty(ups)) {
                response.setList(new ArrayList<>());
                return response;
            }
            List<String> uids = new ArrayList<>();
            ups.forEach(up -> {
                uids.add(up.getUserId());
            });
            list = operatorRepository.findAllByIdIsIn(uids);
        }

        List<OperatorDto> responseList = new ArrayList();
        list.forEach(operator -> {
            responseList.add(convertModelToDto(operator));
        });
        response.setList(responseList);
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws MonitorBaseException {
        Operator operator = operatorRepository.findTopByEmailOrMobile(request.getPhoneOrEmail(), request.getPhoneOrEmail());
        if (operator == null || !request.getPassword().equalsIgnoreCase(operator.getPassword())) {
            throw new MonitorBaseException(ErrEnum.LOGIN_ERROR);
        }
        List<UserProject> ups = userProjectRepository.findAllByUserId(operator.getId());
        TokenMessage tokenMessage = null;
        try {
            tokenMessage = TokenUtils.createToken(operator.getId(), operator.getMobile());
            if (tokenMessage != null) {
                String token = tokenMessage.getToken();
                redisUtil.set(operator.getMobile() + REDIS_TOKEN_KEY, token, passportConf.getTokenExpireTime());
                UserCacheDto cacheDto = new UserCacheDto(operator.getId(), operator.getName(), operator.getMobile(), operator.getEmail(),
                        new HashMap<>(), operator.getRole());
                for (UserProject up : ups) {
                    cacheDto.getProjects().put(up.getProjectId(), up.getRole());
                }
                redisUtil.set(USER_CACHE_KEY + operator.getMobile(), cacheDto);
                return new LoginResponse(token, operator.getRole(), operator.getName(), operator.getEmail(), operator.getMobile());
            }
        } catch (Exception e) {
            log.error("登录失败：{}", e.getMessage(), e);
        }
        return new LoginResponse(ErrEnum.LOGIN_ERROR.getCode(), ErrEnum.LOGIN_ERROR.getMsg());
    }

    @Override
    public MonitorBaseResponse logout() {
        MonitorBaseResponse response = new MonitorBaseResponse();
        try {
            UserCacheDto cacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
            if (cacheDto != null) {
                String key = cacheDto.getMobile() + REDIS_TOKEN_KEY;

                String token = (String) redisUtil.get(key);
                if (org.apache.commons.lang.StringUtils.isNotEmpty(token)) {
                    redisUtil.remove(key);
                }
                response.setReturnSuccess(true);
            } else {
                throw new BaseException(ErrEnum.NOT_LOGIN_ERROR.getCode(),
                        ErrEnum.NOT_LOGIN_ERROR.getMsg());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setReturnSuccess(false);
            response.setReturnErrCode(ErrEnum.LOG_OUT_ERROR.getCode());
            response.setReturnErrMsg(ErrEnum.LOG_OUT_ERROR.getMsg());
        }
        return response;
    }

    @Override
    public UserCacheDto getUserCache(String userId) {
        return (UserCacheDto) redisUtil.get(USER_CACHE_KEY + userId);
    }

    @Override
    public void modifyPwd(ModifyPasswordReq req) throws MonitorBaseException {
        UserCacheDto cacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
        if (cacheDto == null) {
            throw new MonitorBaseException(ErrEnum.NOT_LOGIN_ERROR);
        }
        Operator operator = operatorRepository.findById(cacheDto.getId()).orElse(null);
        if (operator == null) {
            throw new MonitorBaseException(ErrEnum.USER_NOT_EXISTS);
        }
        if (!req.getOldPwd().equalsIgnoreCase(operator.getPassword())) {
            throw new MonitorBaseException(ErrEnum.PWD_ERROR.getCode(), "原始密码错误");
        }
        operator.setPassword(req.getNewPwd());
        operatorRepository.save(operator);
        logout();
    }

    @Override
    public void resetPwd(String userId) throws MonitorBaseException {
        checkPermission();
        Operator operator = operatorRepository.findById(userId).orElse(null);
        if (operator == null) {
            throw new MonitorBaseException(ErrEnum.USER_NOT_EXISTS);
        }
        String password = StringUtil.generatePwd();
        operator.setPassword(Md5Util.md5Encode(password));
        emailUtils.sendEmail(new String[]{operator.getEmail()}, "【监控平台】账号变更通知",
                String.format("监控平台管理员已将您的账户：%s 密码重置为：%s，请尽快登录修改密码", operator.getEmail(), password));
        operatorRepository.save(operator);
    }

    @Override
    public void updateMyInfo(String email, String mobile) throws BaseException {
        UserCacheDto cacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
        if (cacheDto == null) {
            throw new MonitorBaseException(ErrEnum.NOT_LOGIN_ERROR);
        }
        Operator operator = operatorRepository.findById(cacheDto.getId()).orElse(null);
        if (operator == null) {
            throw new MonitorBaseException(ErrEnum.USER_NOT_EXISTS);
        }
        verifyEmailAndMobile(cacheDto.getId(), email, mobile);
        operator.setEmail(email);
        operator.setMobile(mobile);
        operatorRepository.save(operator);
    }

    private void verifyEmailAndMobile(String id, String email, String mobile) throws BaseException {
        Operator operatorEmail;
        Operator operatorMobile;
        if (StringUtils.isEmpty(id)) {
            operatorEmail = operatorRepository.findByEmail(email);
            operatorMobile = operatorRepository.findByMobile(mobile);
        } else {
            operatorEmail = operatorRepository.findByIdIsNotAndEmail(id, email);
            operatorMobile = operatorRepository.findByIdIsNotAndMobile(id, mobile);
        }
        if (operatorEmail != null) {
            throw new BaseException(OPERATOR_EMAIL_ALREADY_EXIST.getCode(), OPERATOR_EMAIL_ALREADY_EXIST.getMsg());
        }
        if (operatorMobile != null) {
            throw new BaseException(OPERATOR_SMS_ALREADY_EXIST.getCode(), OPERATOR_SMS_ALREADY_EXIST.getMsg());
        }
    }

    public Operator convertRequestToModel(OperatorRequest request) {
        Operator operator = new Operator();
        BeanUtils.copyPropertiesIgnorException(request, operator);
        operator.setUpdateTime(new Date());
        return operator;
    }

    public OperatorDto convertModelToDto(Operator operator) {
        OperatorDto dto = new OperatorDto();
        if (null != operator) {
            BeanUtils.copyPropertiesIgnorException(operator, dto);
            return dto;
        }
        return null;
    }
}
