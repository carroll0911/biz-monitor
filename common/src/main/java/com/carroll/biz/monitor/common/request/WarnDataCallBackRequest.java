package com.carroll.biz.monitor.common.request;

import com.carroll.spring.rest.starter.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created on 2017/12/14 11:29 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class WarnDataCallBackRequest extends BaseRequest {

    @ApiModelProperty(value = "应用Id", required = true)
    @NotBlank(message = "应用Id不能为空")
    private String projectId;

    @ApiModelProperty(value = "回调地址", required = true)
    @NotBlank(message = "回调地址不能为空")
    private String callback;

    @ApiModelProperty(value = "启用状态", required = true)
    private boolean enable;

}
