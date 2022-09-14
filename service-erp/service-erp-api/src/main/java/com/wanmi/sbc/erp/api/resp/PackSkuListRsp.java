package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class PackSkuListRsp implements Serializable {

    private static final long serialVersionUID = 5255942860287344142L;
    /**
     * 1：元规格，2：APP权益商品
     */
    private Integer type;

    /**
     * 业务类型 1:实物商品 2：虚拟商品-樊登，3：图书订阅商品 4-历史虚拟商品
     */
    private Integer metaGoodsType;

    /**
     * 类型
     */
    private Integer metaSubGoodsType;

    /**
     * 货品规格ID
     */
    private Long skuId;


    /**
     * 货品规格编码
     */
    private String skuCode;

    /**
     * 货品规格名称
     */
    private String skuName;

    /**
     * 货品ID
     */
    private Long goodsId;

    /**
     * 货品编码
     */
    private String goodsCode;

    /**
     * 货品名称
     */
    private String goodsName;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 仓库ID
     */
    private Long whId;
    /**
     * 仓库CODE
     */
    private String whCode;
    /**
     * 仓库名称
     */
    private String whName;

    /**
     * 产品类型
     */
    private Integer productType;

    /**
     * 产品名称
     */
    private String productName;


    /**
     * 期数
     */
    private Integer totalPeriod;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 货品规格是否有效 1-有效 2-无效
     */
    private Integer skuValidFlag;


    /**
     * 市场价
     */
    private Integer marketPrice;
    /**
     * 定价
     */
    private Integer costPrice;

}
