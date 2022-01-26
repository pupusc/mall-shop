package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.bean.dto.CycleBuyInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 保存订单商品快照请求结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TradeItemSnapshotRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    @NotBlank
    private String customerId;

    /**
     * 开店礼包
     */
    @ApiModelProperty(value = "开店礼包")
    private DefaultFlag storeBagsFlag = DefaultFlag.NO;

    /**
     * 是否组合套装
     */
    @ApiModelProperty(value = "是否组合套装")
    private Boolean suitMarketingFlag;

    /**
     * 组合购场景
     */
    @ApiModelProperty(value = "组合购场景")
    private Integer suitScene;

    /**
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    @ApiModelProperty(value = "是否开团购买")
    private Boolean openGroupon;

    /**
     * 是否购物车购买
     */
    @ApiModelProperty(value = "是否购物车购买")
    private Boolean purchaseBuy = Boolean.TRUE;

    /**
     * 团号
     */
    @ApiModelProperty(value = "团号")
    private String grouponNo;

    /**
     * 商品快照，只包含skuId与购买数量
     */
    @ApiModelProperty(value = "商品快照，只包含skuId与购买数量")
    @NotNull
    private List<TradeItemDTO> tradeItems;

    /**
     * 营销快照
     */
    @ApiModelProperty(value = "营销快照")
    @NotNull
    private List<TradeMarketingDTO> tradeMarketingList;

    /**
     * 快照商品详细信息，包含所属商家，店铺等信息
     */
    @ApiModelProperty(value = "快照商品详细信息，包含所属商家，店铺等信息")
    @NotNull
    private List<GoodsInfoDTO> skuList;

    /**
     * 快照类型--秒杀活动抢购商品订单快照："FLASH_SALE" 预售:PRE_SALE
     */
    @ApiModelProperty(value = "快照类型--秒杀活动抢购商品订单快照：FLASH_SALE 预售: PRE_SALE")
    private String snapshotType;

    /**
     * 是否支持积分商品模式
     */
    @ApiModelProperty(value = "是否支持积分商品模式")
    private Boolean pointGoodsFlag;

    @ApiModelProperty(value = "用户终端的token")
    private String terminalToken;

    /**
     * 周期购信息
     */
    @ApiModelProperty(value = "周期购信息")
    private CycleBuyInfoDTO cycleBuyInfoDTO;
}
