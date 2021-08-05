package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.ShippingCalendarVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeAccountRecordResponse implements Serializable {


    /**
     * 积分
     */
    private Long points;



    /**
     * 积分兑换金额
     */
    private BigDecimal pointsPrice;


}
