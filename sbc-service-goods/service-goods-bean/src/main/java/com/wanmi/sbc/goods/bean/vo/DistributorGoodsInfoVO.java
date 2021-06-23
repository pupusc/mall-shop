package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 分销员商品表
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:02
 * @version: 1.0
 */
@Data
public class DistributorGoodsInfoVO implements Serializable{

    /**
     * 主键
     */
    private String id;

    /**
     * 分销员对应的会员ID
     */
    private String customerId;

    /**
     * 分销商品SKU编号
     */
    private String goodsInfoId;
}
