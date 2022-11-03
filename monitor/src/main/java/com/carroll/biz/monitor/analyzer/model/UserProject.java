package com.carroll.biz.monitor.analyzer.model;

import com.carroll.biz.monitor.analyzer.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户信息
 * Created on 202017/11/22 13:37 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProject extends BaseModel {

    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色
     */
    private Role role;

}
