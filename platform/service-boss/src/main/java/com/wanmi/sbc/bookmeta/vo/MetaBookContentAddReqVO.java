package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 书籍内容描述(MetaBookContent)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookContentAddReqVO implements Serializable {
    private static final long serialVersionUID = -31717324519101494L;
    
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

