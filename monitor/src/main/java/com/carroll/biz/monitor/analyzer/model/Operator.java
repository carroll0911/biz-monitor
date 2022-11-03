package com.carroll.biz.monitor.analyzer.model;

import com.carroll.biz.monitor.analyzer.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 运维人员
 * Created on 202017/11/22 14:21 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class Operator {
    @Id
    private String id;

    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;


    @CreatedDate
    private Date createTime;

    private Date updateTime;
    /**
     * 角色
     */
    private Role role;
    /**
     * 密码
     */
    private String password;
}
