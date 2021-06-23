package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>0元订单批量支付请求参数</p>
 * Created by of628-wenzhi on 2019-07-24-17:03.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class TradeDefaultPayBatchRequest extends BaseRequest {
    private static final long serialVersionUID = 5343882918082462450L;

    /**
     * 0元订单单号集合
     */
    @NotEmpty
    @ApiModelProperty("0元订单单号集合")
    private List<String> tradeIds;

    /**
     * 支付网关(实际不会提交到网关，只做展示使用)
     */
    @NotNull
    @ApiModelProperty("支付网关(实际不会提交到网关，只做展示使用)")
    private PayWay payWay;
}
