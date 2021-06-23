package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import com.wanmi.ares.enums.CheckStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * 商品sku信息
 * Created by sunkun on 2017/9/21.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class GoodsInfoRequest extends BaseMqRequest {

    private static final long serialVersionUID = -4705259762777429610L;

    /**
     * 商品名称
     */
    private String goodsInfoName;

    /**
     * spuid
     */
    private String goodsId;

    /**
     * sku编码
     */
    private String goodsInfoNo;

    /**
     * 上下架时间
     */
    private LocalDate addedTime;

    /**
     * 上下架状态 true:上架 false:下架
     */
    private boolean addedFlag;

    /**
     * 审核状态
     */
    private CheckStatus auditStatus;

    /**
     * 提交审核时间
     */
    private LocalDate submitTime;

    /**
     * 可销售时间(提交审核时间 与 上架时间的最大值)
     */
    private LocalDate saleDate;

    /**
     * 商家id
     */
    private String companyId;

    /**
     * 规格值名称
     */
    private String detailName;

    /**
     * 当前商品的所属分类集
     */
    private List<Long> cateIds;

    /**
     * 当前叶子分类
     */
    private Long leafCateId;

    /**
     * 品牌ID
     */
    private Long brandId;

    /**
     * 店铺分类ID
     */
    private List<Long> storeCateIds;

}
