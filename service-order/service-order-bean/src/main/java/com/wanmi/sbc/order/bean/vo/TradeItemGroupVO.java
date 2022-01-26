package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>按商家，店铺分组的订单商品快照</p>
 * Created by of628-wenzhi on 2017-11-23-下午2:46.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeItemGroupVO implements Serializable {

    private static final long serialVersionUID = 8178188691751557994L;

    /**
     * 订单商品sku
     */
    @ApiModelProperty(value = "订单商品sku")
    private List<TradeItemVO> tradeItems;

    /**
     * 商家与店铺信息
     */
    @ApiModelProperty(value = "商家与店铺信息")
    private SupplierVO supplier;

    /**
     * 订单营销信息
     */
    @ApiModelProperty(value = "订单营销信息")
    private List<TradeItemMarketingVO> tradeMarketingList;

    /**
     * 是否组合套装--原计划是放在TradeItemMarketingVO的
     */
    @ApiModelProperty(value = "是否组合套装")
    private Boolean suitMarketingFlag;

    /**
     * 组合购场景
     */
    @ApiModelProperty(value = "组合购场景")
    private Integer suitScene;

    /**
     * 开店礼包
     */
    @ApiModelProperty(value = "开店礼包")
    private DefaultFlag storeBagsFlag = DefaultFlag.NO;

    /**
     * 快照类型--秒杀活动抢购商品订单快照："FLASH_SALE"
     */
    @ApiModelProperty(value = "快照类型--秒杀活动抢购商品订单快照：FLASH_SALE")
    private String snapshotType;

    /**
     * 下单拼团相关字段
     */
    private TradeGrouponCommitFormVO grouponForm;

    /**
     * 分销佣金总额
     */
    private BigDecimal commission;

    /**
     * 提成人佣金列表
     */
    private List<TradeCommissionVO> commissions = new ArrayList<>();

    /**
     * 周期购信息
     */
    @ApiModelProperty(value = "周期购信息")
    private CycleBuyInfoVO cycleBuyInfo;

}
