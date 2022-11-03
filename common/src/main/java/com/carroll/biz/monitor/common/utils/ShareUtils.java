package com.carroll.biz.monitor.common.utils;

import com.carroll.biz.monitor.common.dto.ProjectDto;
//import com.carroll.cache.RedisUtil;
import com.carroll.cache.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created on 2017/12/11 10:17 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Component
@Slf4j
public class ShareUtils {

    @Autowired
    private RedisUtil redisUtil;

    private static final String PROJECT_KEY_PREFIX = "PROJECT_INFO";

    private static final String KEY_SEPERATOR = "#";

    public ProjectDto loadProjectByTag(String tag) {
        ProjectDto dto = (ProjectDto) redisUtil.get(PROJECT_KEY_PREFIX + KEY_SEPERATOR + tag);
        return dto;
    }

    public ProjectDto loadProjectById(String id) {
        ProjectDto dto = (ProjectDto) redisUtil.get(PROJECT_KEY_PREFIX + KEY_SEPERATOR + id);
        return dto;
    }

    public void shareProject(ProjectDto projectDto) {
        if (null != projectDto) {
            redisUtil.set(PROJECT_KEY_PREFIX + KEY_SEPERATOR + projectDto.getTag(), projectDto);
            redisUtil.set(PROJECT_KEY_PREFIX + KEY_SEPERATOR + projectDto.getId(), projectDto);
        }
    }
}
