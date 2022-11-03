package com.carroll.biz.monitor.common.response;

import com.carroll.spring.rest.starter.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: carroll.he
 * @date 2021/6/9 
 */
@Getter
@Setter
public class MonitorBaseResponse extends BaseResponse {

    private Boolean returnSuccess = true;

    private String returnErrCode;

    private String returnErrMsg;


}
