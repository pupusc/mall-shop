package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.YesOrNo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>拼团活动商品信息表通用查询请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-拼团商品IDList
     */
    private List<String> grouponGoodsIdList;

    /**
     * 批量查询-SKU编号List
     */
    private List<String> goodsInfoIdList;

    /**
     * 批量查询-SPU编号List
     */
    private List<String> goodsIdList;

    /**
     * 批量查询-拼团活动IDList
     */
    private List<String> grouponActivityIdList;
    /**
     * 拼团商品ID
     */
    private String grouponGoodsId;

    /**
     * SKU编号
     */
    private String goodsInfoId;

    /**
     * 拼团价格
     */
    private BigDecimal grouponPrice;

    /**
     * 起售数量
     */
    private Integer startSellingNum;

    /**
     * 限购数量
     */
    private Integer limitSellingNum;

    /**
     * 拼团活动ID
     */
    private String grouponActivityId;

    /**
     * 拼团分类ID
     */
    private String grouponCateId;

    /**
     * 店铺ID
     */
    private String storeId;

    /**
     * SPU编号
     */
    private String goodsId;

    /**
     * 商品销售数量
     */
    private Integer goodsSalesNum;

    /**
     * 订单数量
     */
    private Integer orderSalesNum;

    /**
     * 交易额
     */
    private BigDecimal tradeAmount;

    /**
     * 成团后退单数量
     */
    private Integer refundNum;

    /**
     * 成团后退单金额
     */
    private BigDecimal refundAmount;

    /**
     * 搜索条件:创建时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    /**
     * 搜索条件:更新时间开始
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeBegin;
    /**
     * 搜索条件:更新时间截止
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeEnd;

    /**
     * 活动审核状态，0：待审核，1：审核通过，2：审核不通过
     */
    private AuditStatus auditStatus;

    /**
     *  1: 进行中
     */
    @ApiModelProperty(value = "进行中")
    private Boolean started;

    @ApiModelProperty(value = "是否关联查询goodsInfo")
    private YesOrNo yesOrNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsInfoName;

    @ApiModelProperty(value = "活动状态 0：即将开始 1：进行中 2：已结束 3: 1&2")
    private Integer status;

    /**
     * 多个店铺名称
     */
    @ApiModelProperty(value = "多个店铺id")
    private List<Long> storeIds;

    /**
     * 分销商品审核状态
     */
    @ApiModelProperty(value = "分销商品审核状态")
    private DistributionGoodsAudit distributionGoodsAudit;



}