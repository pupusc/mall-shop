package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.bean.dto.StoreInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeRemedyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-05 11:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeModifyRemedyRequest implements Serializable {

    /**
     * 修改订单信息
     */
    @ApiModelProperty(value = "修改订单信息")
    private TradeRemedyDTO tradeRemedyDTO;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

    /**
     * 店铺信息
     */
    @ApiModelProperty(value = "店铺信息")
    private StoreInfoDTO storeInfoDTO;

}
