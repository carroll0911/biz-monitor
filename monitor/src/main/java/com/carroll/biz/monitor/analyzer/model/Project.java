package com.carroll.biz.monitor.analyzer.model;

import lombok.Getter;
import lombok.Setter;

/**O
 * 产品项目信息
 * Created on 202017/11/22 13:37 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class Project extends BaseModel {

    /**
     * 名称
     */
    private String name;
    /**
     * tag
     */
    private String tag;
    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     */
    private Status status=Status.ENABLED;

    /**
     * 描述
     */
    private String desc;

    public enum Status {
        ENABLED,
        DISABLED
    }

}
