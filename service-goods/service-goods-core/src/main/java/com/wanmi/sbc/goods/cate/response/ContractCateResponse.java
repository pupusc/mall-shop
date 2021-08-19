package com.wanmi.sbc.goods.cate.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 签约分类查询结果实体
 * Created by sunkun on 2017/11/20.
 */
@Data
public class ContractCateResponse {

    /**
     * 签约分类主键
     */
    private Long contractCateId;

    /**
     * 店铺主键
     */
    private Long storeId;


    /**
     * 平台类目id
     */
    private Long cateId;

    /**
     * 平台类目名称
     */
    private String cateName;

    /**
     * 上级平台类目名称(一级/二级)
     */
    private String parentGoodCateNames;

    /**
     * 分类扣率
     */
    private BigDecimal cateRate;

    /**
     * 平台扣率
     */
    private BigDecimal platformCateRate;

    /**
     * 资质图片路径
     */
    private String qualificationPics;
}
