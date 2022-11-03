package com.carroll.biz.monitor.analyzer.model;


import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 自增id序列对象
 * @author zhangjie
 * @create 2017-02-17 11:43
 **/
@Document(collection="sequence")
public class Sequence {

    /**
     * 表名
     */
    private String objName;
    /**
     * 自增序列
     */
    private Long gid;

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }
}
