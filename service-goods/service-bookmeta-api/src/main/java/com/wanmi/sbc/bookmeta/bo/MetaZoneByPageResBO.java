package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图书分区(MetaZone)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Data
public class MetaZoneByPageResBO implements Serializable {

    private Integer id;
    /**
     * 类型：1榜单；2书单；3套系；4版本；
     */     
    private Integer type;
         
    private String name;
    /**
     * 状态：1启用；2停用；
     */     
    private Integer status;
    /**
     * 删除标识
     */     
    private Integer delFlag;
    /**
     * 创建时间
     */     
    private Date createTime;
    /**
     * 更新时间
     */     
    private Date updateTime;
    /**
     * 简介
     */     
    private String descr;
}

