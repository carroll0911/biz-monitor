package com.carroll.biz.monitor.analyzer.service.impl;

import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.repository.MonitorTargetRepository;
import com.carroll.biz.monitor.analyzer.response.MonitorTargetDetailResponse;
import com.carroll.biz.monitor.analyzer.response.MonitorTargetPageResponse;
import com.carroll.biz.monitor.analyzer.dto.MonitorTargetDto;
import com.carroll.biz.monitor.analyzer.enums.ErrEnum;
import com.carroll.biz.monitor.analyzer.model.MonitorTarget;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.request.MonitorTargetPageRequest;
import com.carroll.biz.monitor.analyzer.request.MonitorTargetSaveRequest;
import com.carroll.biz.monitor.analyzer.service.IMonitorTargetService;
import com.carroll.biz.monitor.analyzer.service.IProjectService;
import com.carroll.biz.monitor.analyzer.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警对象管理实现类
 *
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@Service
public class MonitorTargetServiceImpl extends BaseServiceImpl implements IMonitorTargetService {
    @Autowired
    private MonitorTargetRepository repository;
    @Autowired
    private IProjectService projectService;

    @Override
    public MonitorTarget save(MonitorTargetSaveRequest request) throws MonitorBaseException {
        MonitorTarget target=repository.findTopByCodeAndProjectId(request.getCode(),request.getProjectId());
        if(target!=null){
            if(!target.getId().equals(request.getId())){
                throw new MonitorBaseException(ErrEnum.DATA_EXIST);
            }
        } else if(!StringUtils.isEmpty(request.getId())){
            target = repository.findById(request.getId()).orElse(null);
            if(target==null){
                throw new MonitorBaseException(ErrEnum.DATA_NOT_EXIST);
            }
        }
        if(target==null){
            target=new MonitorTarget();
        }
        target.setCode(request.getCode());
        target.setProjectId(request.getProjectId());
        target.setName(request.getName());
        repository.save(target);
        return target;
    }

    @Override
    public MonitorTargetPageResponse page(MonitorTargetPageRequest request) {
        MonitorTargetPageResponse response = new MonitorTargetPageResponse();
        List<MonitorTargetDto> list = new ArrayList<>();
        response.setList(list);
        List<String> projects = getCurrentUserProjects(request.getProjectId());
        if (projects == null) {
            return response;
        }
        Page<MonitorTarget> page = repository.advanceQuery(projects, request.getKeyword(), PageUtil.convertPageRequestToPageable(request));
        if (page != null) {
            response = PageUtil.convertPageToPageResponse(page, response);
            page.getContent().forEach(item -> {
                list.add(convert(item));
            });
        }
        response.setList(list);
        return response;
    }

    private MonitorTargetDto convert(MonitorTarget data){
        MonitorTargetDto dto = new MonitorTargetDto();
        dto.setCode(data.getCode());
        dto.setId(data.getId());
        dto.setName(data.getName());
        dto.setProjectId(data.getProjectId());
        if (!org.apache.commons.lang.StringUtils.isEmpty(data.getProjectId())) {
            Project project = projectService.getByID(data.getProjectId());
            if (project != null) {
                dto.setProjectName(project.getName());
            }
        }
        return dto;
    }

    @Override
    public MonitorTargetDetailResponse detail(String id) throws MonitorBaseException {
        MonitorTarget target = repository.findById(id).orElse(null);
        if(target==null){
            throw new MonitorBaseException(ErrEnum.DATA_NOT_EXIST);
        }
        MonitorTargetDetailResponse response = new MonitorTargetDetailResponse();
        response.setData(target);
        return response;
    }

    @Override
    public void delete(String id) throws MonitorBaseException {
        MonitorTarget target = repository.findById(id).orElse(null);
        if(target==null){
            throw new MonitorBaseException(ErrEnum.DATA_NOT_EXIST);
        }
        repository.delete(target);
    }

    @Cacheable(value = "monitorTargetName#10*60")
    @Override
    public String getTargetName(String code, String projectId) {
        MonitorTarget target=repository.findTopByCodeAndProjectId(code,projectId);
        return target==null?"":target.getName();
    }
}
