package com.wanmi.sbc.order.bean.dto;


import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class ProviderTradeRemedyDTO extends BaseRequest {

    private static final long serialVersionUID = 7232157652419534899L;

    @ApiModelProperty(value = "订单标识")
    private String tradeId;

    @ApiModelProperty(value = "支付方式")
    private PayType payType;

    @ApiModelProperty(value = "收货人Id")
    private String consigneeId;

    @ApiModelProperty(value = "收货地址修改时间")
    private String consigneeUpdateTime;

    @ApiModelProperty(value = "收货地址")
    private String consigneeAddress;

    @ApiModelProperty(value = "发货清单")
    @NotEmpty
    @Valid
    private List<TradeItemDTO> tradeItems;

    @ApiModelProperty(value = "新添加的商品")
    private List<String> newSkuIds;

    @ApiModelProperty(value = "订单价格")
    private TradePriceDTO tradePrice;

    @ApiModelProperty(value = "发票信息")
    private InvoiceDTO invoice;

    @ApiModelProperty(value = "买家备注")
    private String buyerRemark;

    @ApiModelProperty(value = "卖家备注")
    private String sellerRemark;

    @ApiModelProperty(value = "订单附件")
    private String encloses;

    @NotNull
    @ApiModelProperty(value = "配送方式")
    private DeliverWay deliverWay;

    @ApiModelProperty(value = "收货人信息")
    private ConsigneeDTO consignee;

    @ApiModelProperty(value = "发票收货人信息")
    private ConsigneeDTO invoiceConsignee;

    /**
     * 订单营销信息快照
     */
    @ApiModelProperty(value = "订单营销信息快照")
    @NotNull
    private List<TradeMarketingDTO> tradeMarketingList;

    /**
     * 是否强制提交，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    @ApiModelProperty(value = "是否强制提交，用于营销活动有效性校验", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    public boolean forceCommit;

    /**
     * 是否拼团订单
     */
    @ApiModelProperty(value = "是否拼团订单")
    private boolean grouponFlag;

    /**
     * 订单拼团信息
     */
    @ApiModelProperty(value = "订单拼团信息")
    private TradeGrouponDTO tradeGroupon;
}
