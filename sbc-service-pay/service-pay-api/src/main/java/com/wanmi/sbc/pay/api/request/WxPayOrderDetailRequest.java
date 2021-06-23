package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName WxPayOrderDetailRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/9/17 13:56
 **/
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WxPayOrderDetailRequest extends WxPayOrderDetailBaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 微信支付类型
     */
    private WxPayTradeType wxPayTradeType;

    /**
     * 订单号
     */
    private String businessId;

}
