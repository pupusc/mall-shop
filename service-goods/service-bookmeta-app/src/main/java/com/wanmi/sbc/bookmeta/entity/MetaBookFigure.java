package com.wanmi.sbc.bookmeta.entity;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 书籍人物(MetaBookFigure)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 01:28:20
 */
@Table(name = "meta_book_figure")
public class MetaBookFigure implements Serializable {
    private static final long serialVersionUID = -72447978769223605L;
         
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * 书籍id
     */     
    @Column(name = "book_id")
    private Integer bookId;
    /**
     * 作者id
     */     
    @Column(name = "figure_id")
    private Integer figureId;
    /**
     * 类型：1作者；2译者；3绘者；
     */     
    @Column(name = "figure_type")
    private Integer figureType;
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
     * 勾选标识
     */
    @Column(name = "selected")
    private Boolean selected;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getFigureId() {
        return figureId;
    }

    public void setFigureId(Integer figureId) {
        this.figureId = figureId;
    }

    public Integer getFigureType() {
        return figureType;
    }

    public void setFigureType(Integer figureType) {
        this.figureType = figureType;
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

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}

