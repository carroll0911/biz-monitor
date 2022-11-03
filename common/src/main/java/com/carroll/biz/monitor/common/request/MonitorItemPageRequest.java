package com.carroll.biz.monitor.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 2017/12/5 17:43 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class MonitorItemPageRequest extends BmPageRequest {
    @ApiModelProperty("搜索关键字")
    private String keyword;
}
