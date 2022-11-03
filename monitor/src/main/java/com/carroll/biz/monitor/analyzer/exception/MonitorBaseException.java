package com.carroll.biz.monitor.analyzer.exception;

import com.carroll.biz.monitor.analyzer.enums.ErrEnum;
import com.carroll.spring.rest.starter.BaseException;

/**
 * @author: hehongbo
 * @date 2019/9/9
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public class MonitorBaseException extends BaseException {
    public MonitorBaseException(String code, String msg) {
        super(code, msg);
    }

    public MonitorBaseException(ErrEnum errEnum) {
        super(errEnum.getCode(), errEnum.getMsg());
    }
}
