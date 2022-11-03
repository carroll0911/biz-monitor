package com.carroll.biz.monitor.analyzer.service;/**
 * Created by core_ on 2017/12/19.
 */

import com.carroll.biz.monitor.collector.annotation.Monitor;

/**
 * Created on 202017/12/19 14:26 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface ITestService {
    @Monitor(tag = "test")
    int testMonitor2(int n);
}
