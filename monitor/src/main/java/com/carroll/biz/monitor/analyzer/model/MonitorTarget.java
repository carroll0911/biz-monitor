package com.carroll.biz.monitor.analyzer.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 告警对象
 *
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class MonitorTarget extends BaseModel {

    private String code;

    private String projectId;

    private String name;
}
