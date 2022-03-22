package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradePurchaseRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = -7606864055013901869L;

    /**
     * 是否强制提交，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    @ApiModelProperty(value = "是否强制提交", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    public boolean forceCommit;

    /**
     * 下单用户
     */
    @ApiModelProperty(value = "下单用户")
    private CustomerSimplifyOrderCommitVO customer;

    /**
     * 商品信息
     */
    private List<TradeItemDTO> tradeItems;

    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
