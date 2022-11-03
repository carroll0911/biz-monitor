package com.carroll.biz.monitor.common.response;

import com.carroll.biz.monitor.common.dto.OperatorDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created on 2017/12/5 16:07 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class OperatorPageResponse extends PageResponse{

    private List<OperatorDto> list;
}
