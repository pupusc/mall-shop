package com.wanmi.ares.source.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.ms.util.CustomLocalDateDeserializer;
import com.wanmi.ms.util.CustomLocalDateSerializer;
import com.wanmi.ares.enums.CheckStatus;
import com.wanmi.ares.source.model.root.base.BaseData;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 商品基础信息
 * Created by sunkun on 2017/9/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class GoodsInfo extends BaseData{

    private static final long serialVersionUID = -5621196272385871899L;

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

    private String goodsInfoId;

    /**
     * 上架时间
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
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
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate submitTime;

    /**
     * 可销售时间(提交审核时间 与 上架时间的最大值)
     */
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate saleDate;

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
     * 品牌编号
     */
    private Long brandId;

    /**
     * 商家id
     */
    private String companyId;

    /**
     * 当前商品的所属店铺分类集
     */
    private List<Long> storeCateIds;

    /**
     * 当前叶子分类
     */
    private List<Long> leafStoreCateIds;
}

