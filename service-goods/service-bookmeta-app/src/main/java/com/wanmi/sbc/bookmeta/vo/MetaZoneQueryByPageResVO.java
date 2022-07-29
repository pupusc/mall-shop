package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 图书分区(MetaZone)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Data
public class MetaZoneQueryByPageResVO implements Serializable {
    private static final long serialVersionUID = -96424797593923687L;
    
    private Integer id;
    /**
     * 类型：1榜单；2书单；3套系；4版本；
     */
    private Integer type;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态：1启用；2停用；
     */
    private Integer status;
    /**
     * 简介
     */
    private String descr;
}

