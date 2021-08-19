package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * <p>根据父子订单号查询交易记录参数结构</p>
 * Created by of628-wenzhi on 2019-07-24-21:27.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeRecordByOrderOrParentCodeRequest extends PayBaseRequest {
    private static final long serialVersionUID = -3403898642590110298L;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderId;

    /**
     * 父订单号
     */
    @ApiModelProperty(value = "父订单号")
    private String parentId;

    @Override
    public void checkParam() {
        Validate.isTrue(StringUtils.isNotEmpty(orderId) || StringUtils.isNotEmpty(parentId), NULL_EX_MESSAGE,
                "tid | parentTid");
    }
}
