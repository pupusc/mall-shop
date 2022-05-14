package com.wanmi.sbc.goods.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 书籍推荐(MetaBookRcmmd)实体类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
public class MetaBookRcmmd implements Serializable {
    private static final long serialVersionUID = -45242953464726642L;
    
    private Integer id;
    /**
     * 业务id
     */
    private Integer bizId;
    /**
     * 业务type：1获奖推荐；2编辑推荐；3媒体推荐；4专业机构推荐；5名家推荐；6书中引用推荐；7讲稿引用推荐；
     */
    private Integer bizType;
    /**
     * 业务时间
     */
    private Date bizTime;
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
     * 描述信息
     */
    private String descr;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public Date getBizTime() {
        return bizTime;
    }

    public void setBizTime(Date bizTime) {
        this.bizTime = bizTime;
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

}

