package com.carroll.biz.monitor.analyzer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: hehongbo
 * @date 2019/12/11
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Setter
@Getter
public class ChartSeriesItem {

    private String name;
    private String type;
    private List<Object> data;
}
