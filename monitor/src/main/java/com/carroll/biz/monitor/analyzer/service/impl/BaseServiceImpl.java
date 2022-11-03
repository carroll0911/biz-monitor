package com.carroll.biz.monitor.analyzer.service.impl;

import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.enums.ErrEnum;
import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hehongbo
 * @date 2019/9/27
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public class BaseServiceImpl {

    public void checkPermission() throws MonitorBaseException {
        UserCacheDto cacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
        if (!Role.SUPPER.equals(cacheDto.getRole())) {
            throw new MonitorBaseException(ErrEnum.NO_PERMISSION_ERROR);
        }
    }

    public boolean checkPermission(String projectId) {
        return checkPermission(projectId, false);
    }

    public boolean checkPermission(String projectId, boolean needAdmin) {
        // 校验是否有权限
        UserCacheDto cacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
        if (cacheDto == null || (!Role.SUPPER.equals(cacheDto.getRole()) &&
                (CollectionUtils.isEmpty(cacheDto.getProjects()) || !cacheDto.getProjects().containsKey(projectId)))) {
            return false;
        }
        if (needAdmin && !Role.SUPPER.equals(cacheDto.getRole()) && !Role.ADMIN.equals(cacheDto.getProjects().get(projectId))) {
            return false;
        }
        return true;
    }

    public List<String> getCurrentUserProjects(String projectId) {
        UserCacheDto userCacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
        if (userCacheDto == null || (CollectionUtils.isEmpty(userCacheDto.getProjects()) && !Role.SUPPER.equals(userCacheDto.getRole()))) {
            // 当前用户不是超级管理员且未关联任何应用直接返回空列表
            return null;
        }
        List<String> projects = new ArrayList<>();
        if (Role.SUPPER.equals(userCacheDto.getRole())) {
            if (!org.apache.commons.lang.StringUtils.isEmpty(projectId)) {
                projects.add(projectId);
            }
        } else {
            if (!org.apache.commons.lang.StringUtils.isEmpty(projectId)) {
                if (!userCacheDto.getProjects().containsKey(projectId)) {
                    return null;
                } else {
                    projects.add(projectId);
                }
            } else {
                projects.addAll(userCacheDto.getProjects().keySet());
            }
        }
        return projects;
    }
}
