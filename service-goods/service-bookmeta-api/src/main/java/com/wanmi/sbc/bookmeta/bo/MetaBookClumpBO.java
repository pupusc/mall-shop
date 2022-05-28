package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 丛书(MetaBookClump)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:07
 */
@Data
public class MetaBookClumpBO implements Serializable {
    private static final long serialVersionUID = 410758902116412645L;
         
    private Integer id;
    /**
     * 出版社id
     */     
    private Integer publisherId;
    /**
     * 名称
     */     
    private String name;
    /**
     * 图片
     */     
    private String image;
    /**
     * 册数
     */     
    private Integer volumeCount;
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
     * 描述
     */     
    private String descr;
}

