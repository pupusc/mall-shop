package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;

/**
 * 标签(MetaLabel)实体类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
public class MetaLabelQueryByIdResVO implements Serializable {
    private static final long serialVersionUID = 585894274349153324L;
    
    private Integer id;
    /**
     * 目录id
     */
    private Integer labelCateId;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态：0停用；1启用；
     */
    private Integer status;
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
    /**
     * 说明
     */
    private String descr;
    /**
     * 顺序
     */
    private Integer seq;
   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLabelCateId() {
        return labelCateId;
    }

    public void setLabelCateId(Integer labelCateId) {
        this.labelCateId = labelCateId;
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

}

