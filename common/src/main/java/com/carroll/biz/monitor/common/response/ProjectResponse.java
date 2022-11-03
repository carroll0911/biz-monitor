package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.ProjectDto;
import lombok.Data;

/**
 * Created on 2017/12/14 11:31 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
public class ProjectResponse extends MonitorBaseResponse {
    private ProjectDto data;
}
