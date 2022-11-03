package com.carroll.biz.monitor.analyzer.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carroll.biz.monitor.analyzer.model.BaseServiceMonitorItem;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.service.IBSMonitorItemService;
import com.carroll.biz.monitor.analyzer.service.IProjectService;
import com.carroll.biz.monitor.collector.component.DataSender;
import com.carroll.utils.OkHttpUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RabbitMQ监控时任务
 * Created on 2018/11/22 13:18 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Component
public class RabbitMqMonitorJob {
    @Autowired
    private IBSMonitorItemService bsMonitorItemService;
    @Autowired
    private IProjectService projectService;
    @Autowired
    private DataSender dataSender;

    @XxlJob("rabbitMqMonitorJob")
    public void execute(String s) throws Exception {
        List<Project> projects = projectService.findAll();
        List<BaseServiceMonitorItem> bsmis = null;
        Map<String, Integer> rs = null;
        for (Project p : projects) {
            bsmis = bsMonitorItemService.findAllByTagAndProjectId("RABBITMQ", p.getId());
            for (BaseServiceMonitorItem bsmi : bsmis) {
                rs = mqSumInfo(bsmi);
                if (rs != null) {
                    rs.forEach((que, count) -> {
                        if (count != null && bsmi.getWarnLine() != null && count > bsmi.getWarnLine()) {
                            dataSender.send(bsmi.getTag(), false, null, count, que);
                        } else {
                            dataSender.send(bsmi.getTag(), true, null, count, que);
                        }
                    });
                }
            }
        }
        XxlJobHelper.handleSuccess();
    }

    private Map<String, Integer> mqSumInfo(BaseServiceMonitorItem item) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", item.getAuthorization());
        JSONArray ja = null;
        try {
            OkHttpUtil.OkResponse response = OkHttpUtil.doGet(item.getUri(), "", headers);
            log.info("rabbitMq queues state:{}", response.getResult());
            if (response.isSuccessful() && !StringUtils.isEmpty(response.getResult())) {
                ja = JSONObject.parseArray(response.getResult());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Map<String, Integer> queues = new HashMap<>();
        JSONObject jo = null;
        Integer msgcount = 0;
        if (ja != null && ja.size() > 0) {
            for (int i = 0; i < ja.size(); i++) {
                jo = ja.getJSONObject(i);
                if (jo != null) {
                    msgcount = jo.getInteger("messages");
                    if (msgcount != null && msgcount > 0) {
                        queues.put(String.format("%s:%s", jo.getString("name"), jo.getString("node")), msgcount);
                    }
                }
            }
        }

        return queues;
    }


}
