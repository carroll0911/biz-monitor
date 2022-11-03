package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.ConstantDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created on 2017/12/11 10:59 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class MonitorItemConstantResponse extends MonitorBaseResponse {
    List<ConstantDto> list;
}
