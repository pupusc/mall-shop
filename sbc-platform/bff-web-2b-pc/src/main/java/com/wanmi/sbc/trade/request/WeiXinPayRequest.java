package com.wanmi.sbc.trade.request;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * 微信扫码支付请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("微信扫码支付请求参数")
public class WeiXinPayRequest extends BaseRequest {

    private static final long serialVersionUID = -2855598681583948807L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id，用于单笔订单支付场景，单笔支付时不能为空", dataType = "String", required = true)
    private String tid;

    /**
     * 父订单id
     */
    @ApiModelProperty(value = "父订单id，用于多笔订单合并支付场景，合并支付时不能为空", dataType = "String", required = true)
    private String parentTid;

    @Override
    public void checkParam() {
        Validate.isTrue(StringUtils.isNotEmpty(tid) || StringUtils.isNotEmpty(parentTid), NULL_EX_MESSAGE,
                "tid | parentTid");
    }
}
