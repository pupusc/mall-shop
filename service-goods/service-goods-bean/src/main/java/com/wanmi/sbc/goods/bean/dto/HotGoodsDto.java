package com.wanmi.sbc.goods.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_hot_goods
 * @author 
 */
@Data
public class HotGoodsDto implements Serializable {
    private String id;

    private String spuId;

    /**
     * 1-商品  2-书单
     */
    private Integer type;

    private Integer sort;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}