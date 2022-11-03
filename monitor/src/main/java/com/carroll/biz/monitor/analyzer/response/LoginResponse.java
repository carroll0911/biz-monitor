package com.carroll.biz.monitor.analyzer.response;

import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 登录返回对象
 *
 * @author: hehongbo
 * @date 2019/9/9
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends MonitorBaseResponse {

    /**
     * 登录用户token
     */
    private String token;
    /**
     * 角色
     */
    private Role role;
    /**
     * 姓名
     */
    private String name;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    public LoginResponse(String code, String msg) {
        setReturnErrCode(code);
        setReturnErrMsg(msg);
    }
}
