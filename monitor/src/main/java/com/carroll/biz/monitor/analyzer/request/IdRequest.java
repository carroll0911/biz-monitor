package com.carroll.biz.monitor.analyzer.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author: hehongbo
 * @date 2019/10/9
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class IdRequest {
    @ApiModelProperty(value = "数据id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;
}
