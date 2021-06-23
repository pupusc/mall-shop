package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class TradeItemConfirmSettlementRequest implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
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
     * 是否开团购买(true:开团 false:参团 null:非拼团购买)
     */
    @ApiModelProperty(value = "是否开团购买")
    private Boolean openGroupon;

    /**
     * 团号
     */
    @ApiModelProperty(value = "团号")
    private String grouponNo;

    /**
     * 商品快照，只包含skuId与购买数量
     */
    @ApiModelProperty(value = "商品快照，只包含skuId与购买数量")
    private List<TradeItemDTO> tradeItems;

    /**
     * 营销快照
     */
    @ApiModelProperty(value = "营销快照")
    private List<TradeMarketingDTO> tradeMarketingList;

    /**
     * 快照商品详细信息，包含所属商家，店铺等信息
     */
    @ApiModelProperty(value = "快照商品详细信息，包含所属商家，店铺等信息")
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
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    private String inviteeId;

    /**
     * 采购单SKU集合
     */
    private List<String> skuIds;

    /**
     * 是否开启社交分销
     */
    private DefaultFlag openFlag;

    /**
     * 分销渠道
     */
    private ChannelType channelType;

    /**
     * 分销渠道信息
     */
    @ApiModelProperty(value = "分销渠道信息")
    private DistributeChannel distributeChannel;

    /**
     * 是否强制确认，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    @ApiModelProperty(value = "是否强制确认，用于营销活动有效性校验,true: 无效依然提交， false: 无效做异常返回")
    public boolean forceConfirm;

    @ApiModelProperty("收货地址区的地址码")
    public String areaId;

}
