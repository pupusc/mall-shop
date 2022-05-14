package com.wanmi.sbc.goods.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 标签目录(MetaLabelCate)实体类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
public class MetaLabelCate implements Serializable {
    private static final long serialVersionUID = 712339187462884900L;
    
    private Integer id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父级id
     */
    private Integer parentId;
    /**
     * 路径
     */
    private String path;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标识
     */
    private Integer delFlag;


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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

}

