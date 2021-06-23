package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 10:06
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class VerifyTradeMarketingRequest implements Serializable {

    private static final long serialVersionUID = -4223077676908497569L;

    @ApiModelProperty(value = "订单营销信息列表")
    private List<TradeMarketingDTO> tradeMarketingList;

    @ApiModelProperty(value = "赠品列表")
    private List<TradeItemDTO> oldGifts;

    @ApiModelProperty(value = "订单列表")
    private List<TradeItemDTO> tradeItems;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "是否提交", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean isFoceCommit;
}
