package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 人物获奖(MetaFigureAward)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:10
 */
@Data
public class MetaFigureAwardBO implements Serializable {
    private static final long serialVersionUID = 540009968572671932L;
         
    private Integer id;
    /**
     * 人物id
     */     
    private Integer figureId;
    /**
     * 奖项id
     */     
    private Integer awardId;
    /**
     * 获奖时间
     */     
    private Date awardTime;
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

