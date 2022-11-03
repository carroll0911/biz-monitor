package com.carroll.biz.monitor.analyzer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogData {
    // 监控点调用参数
    private String params;
    // 监控点调用结果
    private String response;

    /**
     * 方法执行所用时间
     */
    private Long useTimeMs;
    /**
     * 监控超时时间
     */
    private Long timeoutMs;
    /**
     * 调用链ID
     */
    private String traceId;

    private Long time;
}
