package com.carroll.biz.monitor.analyzer.response;

import com.carroll.biz.monitor.analyzer.dto.ChartSeriesItem;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 告警统计曲线图 请求结果
 *
 * @author: hehongbo
 * @date 2019/12/11
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class ItemSummary4LineResponse extends MonitorBaseResponse {

    private List<Date> dates;

    private List<String> itemNames;

    private List<ChartSeriesItem> datas;
}
