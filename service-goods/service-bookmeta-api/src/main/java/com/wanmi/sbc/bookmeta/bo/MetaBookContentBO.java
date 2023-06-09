package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 书籍内容描述(MetaBookContent)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:08
 */
@Data
public class MetaBookContentBO implements Serializable {
    private static final long serialVersionUID = -55471818891341473L;
         
    private Integer id;
    /**
     * 书籍id
     */     
    private Integer bookId;
    /**
     * 1简介；2目录；3摘录；4序言；5前言；
     */     
    private Integer type;
    /**
     * 关联的人物
     */     
    private Integer figureId;
    /**
     * 关联的人物名称
     */
    private String figureName;
    /**
     * 内容
     */     
    private String content;
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
}

