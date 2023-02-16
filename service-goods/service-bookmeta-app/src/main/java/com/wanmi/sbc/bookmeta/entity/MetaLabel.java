package com.wanmi.sbc.bookmeta.entity;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 标签(MetaLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 01:28:20
 */
@Table(name = "meta_label")
public class MetaLabel implements Serializable {
    private static final long serialVersionUID = 262954670573753788L;
         
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 名称
     */     
    @Column(name = "name")
    private String name;
    /**
     * 状态：1启用；2停用；
     */     
    @Column(name = "status")
    private Integer status;
    /**
     * 创建时间
     */     
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */     
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 删除标识
     */     
    @Column(name = "del_flag")
    private Integer delFlag;
    /**
     * 说明
     */     
    @Column(name = "descr")
    private String descr;
    /**
     * 顺序
     */     
    @Column(name = "seq")
    private Integer seq;
    /**
     * 父级id
     */     
    @Column(name = "parent_id")
    private Integer parentId;
    /**
     * 类型：1目录；2标签；
     */     
    @Column(name = "type")
    private Integer type;
    /**
     * 场景：1适读对象；
     */     
    @Column(name = "scene")
    private Integer scene;
    /**
     * 路径：下划线分隔
     */     
    @Column(name = "path")
    private String path;

    @Column(name = "is_static")
    private Integer isStatic;

    @Column(name = "is_run")
    private Integer isRun;

    @Column(name = "run_from_time")
    private java.sql.Date runFromTime;

    @Column(name = "run_to_time")
    private java.sql.Date runToTime;

    public Integer getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(Integer isStatic) {
        this.isStatic = isStatic;
    }

    public Integer getIsRun() {
        return isRun;
    }

    public void setIsRun(Integer isRun) {
        this.isRun = isRun;
    }

    public java.sql.Date getRunFromTime() {
        return runFromTime;
    }

    public void setRunFromTime(java.sql.Date runFromTime) {
        this.runFromTime = runFromTime;
    }

    public java.sql.Date getRunToTime() {
        return runToTime;
    }

    public void setRunToTime(java.sql.Date runToTime) {
        this.runToTime = runToTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getScene() {
        return scene;
    }

    public void setScene(Integer scene) {
        this.scene = scene;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}

