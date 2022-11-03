package com.carroll.biz.monitor.analyzer.response;

import com.carroll.biz.monitor.analyzer.dto.ProjectListDto;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: hehongbo
 * @date 2019/9/4
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class ProjectListResponse extends MonitorBaseResponse {

    private List<ProjectListDto> list;
}
