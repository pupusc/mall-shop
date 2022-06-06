package com.wanmi.sbc.bookmeta.entity;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 行业数据文件(MetaBookIndustryFile)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 01:28:20
 */
@Table(name = "meta_book_industry_file")
public class MetaBookIndustryFile implements Serializable {
    private static final long serialVersionUID = 274827418774330099L;
         
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 文件地址
     */     
    @Column(name = "file_url")
    private String fileUrl;
    /**
     * 状态：1待同步；2同步中；3已同步；4同步失败；
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
     * 描述信息，失败原因等
     */     
    @Column(name = "descr")
    private String descr;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

}

