package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemGroupDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeQueryPurchaseInfoRequest implements Serializable {

    /**
     * 交易单分组信息
     */
    @ApiModelProperty(value = "交易单分组信息")
    private TradeItemGroupDTO tradeItemGroupDTO;

    /**
     * 赠品列表
     */
    @ApiModelProperty(value = "赠品列表")
    private List<TradeItemDTO> tradeItemList;
    /**
     * 换购列表
     */
    @ApiModelProperty(value = "换购列表")
    private List<TradeItemDTO> markupItemList;

}
