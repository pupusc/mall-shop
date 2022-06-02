package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 书组(MetaBookGroup)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:08
 */
@Data
public class MetaBookGroupBO implements Serializable {
    private static final long serialVersionUID = 169505675050478174L;

    private Integer id;
    /**
     * 类型：1套系；2不同版本；
     */     
    private Integer type;
    /**
     * 名称
     */     
    private String name;
    /**
     * 图片
     */     
    private String image;
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
     * 介绍
     */     
    private String descr;
}

