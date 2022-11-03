package com.carroll.biz.monitor.collector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hehongbo
 * @Date 2017-07-25 18:06
 **/
@Component
@ConfigurationProperties(prefix = "monitor.kafka")
@Data
public class MonitorKafkaConfig {
    private String brokers;
    private String group;
    private int bachSize;
    private int lingerMs;
    private int bufferMemory;
    private String autoCommitIntervalMs;
    private String sessionTimeoutMs;
    private String heartbeatIntervalMs;
    private String autoOffsetReset;
    private int maxPollRecords;
    private int concurrencey;
    private int pollTimeout;
    private int maxBlockMs = 3000;
}
