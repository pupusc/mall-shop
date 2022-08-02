package com.wanmi.sbc.bookmeta.entity;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 图书分区关联图书(MetaZoneBook)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:37
 */
@Table(name = "meta_zone_book")
public class MetaZoneBook implements Serializable {
    private static final long serialVersionUID = -19560324903307879L;

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 主题id
     */     
    @Column(name = "zone_id")
    private Integer zoneId;
    /**
     * 关联图书
     */     
    @Column(name = "book_id")
    private Integer bookId;
    /**
     * 删除标识
     */     
    @Column(name = "del_flag")
    private Integer delFlag;
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


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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

}

