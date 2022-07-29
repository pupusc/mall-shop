package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * 图书分区(MetaZone)实体类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Data
public class MetaZoneQueryByPageReqBO implements Serializable {
    private static final long serialVersionUID = 310448191517922704L;
    
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
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

