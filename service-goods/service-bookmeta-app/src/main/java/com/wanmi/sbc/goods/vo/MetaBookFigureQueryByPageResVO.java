package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;

/**
 * 书籍人物(MetaBookFigure)实体类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
public class MetaBookFigureQueryByPageResVO implements Serializable {
    private static final long serialVersionUID = 935920330679924664L;
    
    private Integer id;
    /**
     * 书籍id
     */
    private Integer bookId;
    /**
     * 作者id
     */
    private Integer figureId;
    /**
     * 类型：1作者；2译者；3绘画人；
     */
    private Integer figureType;
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

}

