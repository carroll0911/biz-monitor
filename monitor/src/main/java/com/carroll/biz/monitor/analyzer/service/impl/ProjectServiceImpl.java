package com.carroll.biz.monitor.analyzer.service.impl;

import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.repository.OperatorRepository;
import com.carroll.biz.monitor.analyzer.repository.ProjectRepository;
import com.carroll.biz.monitor.analyzer.repository.UserProjectRepository;
import com.carroll.biz.monitor.analyzer.response.ProjectDetailResponse;
import com.carroll.biz.monitor.analyzer.service.IProjectService;
import com.carroll.biz.monitor.analyzer.dto.OperatorDtoV2;
import com.carroll.biz.monitor.analyzer.dto.ProjectDetailDto;
import com.carroll.biz.monitor.analyzer.dto.ProjectListDto;
import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.enums.ErrEnum;
import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.model.Operator;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.model.UserProject;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import com.carroll.biz.monitor.common.dto.ProjectDto;
import com.carroll.biz.monitor.common.request.ProjectRequest;
import com.carroll.biz.monitor.common.request.ProjectUpdateRequest;
import com.carroll.biz.monitor.common.response.ProjectResponse;
import com.carroll.biz.monitor.common.utils.ShareUtils;
import com.carroll.spring.rest.starter.BaseException;
import com.carroll.utils.BeanUtils;
import com.carroll.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 项目管理
 * Created on 202017/11/22 13:46 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserProjectRepository userProjectRepository;
    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private ShareUtils shareUtils;

    @Override
    @Cacheable(value = "project#2*60*60")
    public Project getByTag(String tag) {
        return projectRepository.findByTag(tag);
    }

    @Override
    @Cacheable(value = "project#2*60*60")
    public Project getByID(String id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Override
    public void refreshProject() {
        List<Project> list = projectRepository.findAll();
        list.forEach(project -> {
            ProjectDto projectDto = convertModelToDto(project);
            shareUtils.shareProject(projectDto);
        });
    }

    @Override
    public ProjectResponse save(ProjectRequest request) throws BaseException {
        checkPermission();
        ProjectResponse response = new ProjectResponse();
        verify(null, request.getTag());
        Project project = convertRequestToModel(request);
        String password = password();
        project.setPassword(password);
        projectRepository.save(project);
        saveUserProject(project, request.getUsers(), request.getAdmins());
        ProjectDto data = convertModelToDto(project);
        shareUtils.shareProject(data);
        response.setData(data);
        return response;
    }

    private void verify(String id, String tag) throws BaseException {
        Project proExist;
        if (StringUtils.isEmpty(id)) {
            proExist = projectRepository.findByTag(tag);
        } else {
            proExist = projectRepository.findByIdIsNotAndTag(id, tag);
        }
        if (null != proExist) {
            throw new BaseException(ErrEnum.PROJECT_TAG_ALREADY_EXIST.getCode(), ErrEnum.PROJECT_TAG_ALREADY_EXIST.getMsg());
        }
    }

    @Override
    public ProjectResponse update(ProjectUpdateRequest request) throws BaseException {
        if (!checkPermission(request.getId(), true)) {
            throw new MonitorBaseException(ErrEnum.NO_PERMISSION_ERROR);
        }
        ProjectResponse response = new ProjectResponse();
        Project project = projectRepository.findById(request.getId()).orElse(null);
        if (null == project) {
            throw new BaseException(ErrEnum.DATA_NOT_EXIST.getCode(), ErrEnum.DATA_NOT_EXIST.getMsg());
        }
        verify(request.getId(), request.getTag());
        BeanUtils.copyPropertiesIgnorException(request, project);
        ProjectDto data = convertModelToDto(project);
        projectRepository.save(project);
        saveUserProject(project, request.getUsers(), request.getAdmins());
        shareUtils.shareProject(data);
        response.setData(data);
        return response;
    }

    private void saveUserProject(Project project, List<String> userIds, List<String> admins) {
        List<UserProject> ups = new ArrayList<>();
        userProjectRepository.deleteAllByProjectId(project.getId());
        UserProject up = null;
        if (!CollectionUtils.isEmpty(admins)) {
            for (String uid : admins) {
                if (!StringUtils.isEmpty(uid)) {
                    up = new UserProject(project.getId(), uid, Role.ADMIN);
                    ups.add(up);
                }
            }
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            for (String uid : userIds) {
                if (!StringUtils.isEmpty(uid)) {
                    //判断是否已经在管理员列表中存在
                    if (!(CollectionUtils.isEmpty(admins) && admins.contains(uid))) {
                        up = new UserProject(project.getId(), uid, Role.NORMAL);
                        ups.add(up);
                    }
                }
            }
        }
        if (!ups.isEmpty()) {
            userProjectRepository.saveAll(ups);
        }
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    private List<Project> findAllByUser() {
        UserCacheDto cacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
        if (cacheDto == null || (!Role.SUPPER.equals(cacheDto.getRole()) && CollectionUtils.isEmpty(cacheDto.getProjects()))) {
            return null;
        }
        if (Role.SUPPER.equals(cacheDto.getRole())) {
            return projectRepository.findAll();
        } else {
            return projectRepository.findByIdIsIn(new ArrayList<>(cacheDto.getProjects().keySet()));
        }
    }

    @Override
    public List<ProjectListDto> list() {
        List<Project> projects = findAllByUser();
        if (CollectionUtils.isEmpty(projects)) {
            return null;
        }
        List<ProjectListDto> list = new ArrayList<>();
        ProjectListDto dto = null;
        for (Project p : projects) {
            dto = new ProjectListDto();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setTag(p.getTag());
            dto.setStatus(p.getStatus());
            dto.setAdmins(converUps(userProjectRepository.findAllByProjectIdAndRole(p.getId(), Role.ADMIN)));
            list.add(dto);
        }
        return list;
    }

    @Override
    public ProjectDetailResponse detail(String projectId) throws MonitorBaseException {
        if (!checkPermission(projectId)) {
            throw new MonitorBaseException(ErrEnum.DATA_NOT_EXIST);
        }
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new MonitorBaseException(ErrEnum.DATA_NOT_EXIST);
        }
        ProjectDetailDto detailDto = new ProjectDetailDto();
        detailDto.setId(projectId);
        detailDto.setName(project.getName());
        detailDto.setPassword(project.getPassword());
        detailDto.setTag(project.getTag());

        List<UserProject> ups = userProjectRepository.findAllByProjectId(projectId);
        if (CollectionUtils.isEmpty(ups)) {
            detailDto.setUsers(new ArrayList<>());
        } else {
            detailDto.setUsers(converUps(ups));
        }
        ProjectDetailResponse response = new ProjectDetailResponse();
        response.setData(detailDto);
        return response;
    }

    @Override
    public void resetPassword(String projectId) throws BaseException {
        Project project = checkUpdate(projectId);
        project.setPassword(StringUtil.generatePwd());
        projectRepository.save(project);
    }

    @Override
    public void changeStatus(String projectId, Project.Status status) throws BaseException {
        Project project = checkUpdate(projectId);
        project.setStatus(status);
        projectRepository.save(project);
    }

    private Project checkUpdate(String projectId) throws BaseException {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (null == project) {
            throw new BaseException(ErrEnum.DATA_NOT_EXIST.getCode(), ErrEnum.DATA_NOT_EXIST.getMsg());
        }
        if (!checkPermission(projectId, true)) {
            throw new MonitorBaseException(ErrEnum.NO_PERMISSION_ERROR);
        }
        return project;
    }

    private List<OperatorDtoV2> converUps(List<UserProject> ups) {
        List<String> uids = new ArrayList<>();
        Map<String, Role> roleMap = new HashMap<>();
        ups.forEach(u -> {
            uids.add(u.getUserId());
            roleMap.put(u.getUserId(), u.getRole());
        });
        List<Operator> users = operatorRepository.findAllByIdIsIn(uids);
        List<OperatorDtoV2> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(users)) {
            return result;
        }
        OperatorDtoV2 user = null;
        for (Operator op : users) {
            user = new OperatorDtoV2();
            user.setEmail(op.getEmail());
            user.setId(op.getId());
            user.setMobile(op.getMobile());
            user.setName(op.getName());
            user.setRole(roleMap.get(op.getId()));
            result.add(user);
        }
        return result;
    }

    /**
     * 生成project的密码
     *
     * @return
     */
    private String password() {
        StringBuilder pwd = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int num = rand.nextInt(3);
            switch (num) {
                case 0:
                    char c1 = (char) (rand.nextInt(26) + 'a');//生成随机小写字母
                    pwd.append(c1);
                    break;
                case 1:
                    char c2 = (char) (rand.nextInt(26) + 'A');//生成随机大写字母
                    pwd.append(c2);
                    break;
                case 2:
                    pwd.append(rand.nextInt(10));//生成随机数字
                    break;
                default:
                    break;
            }
        }
        return pwd.toString();
    }

    private ProjectDto convertModelToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        BeanUtils.copyPropertiesIgnorException(project, dto);
        return dto;
    }

    private Project convertRequestToModel(ProjectRequest request) {
        Project project = new Project();
        BeanUtils.copyPropertiesIgnorException(request, project);
        return project;
    }
}
