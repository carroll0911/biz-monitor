package com.carroll.biz.monitor.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created on 2017/11/30 17:33 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class IdRequest extends BmBaseRequest {
    @ApiModelProperty(value = "数据id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;
}
