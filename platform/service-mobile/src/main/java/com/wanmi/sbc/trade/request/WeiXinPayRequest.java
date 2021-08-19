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
@ApiModel
public class WeiXinPayRequest extends BaseRequest {

    private static final long serialVersionUID = -2855598681583948807L;

    /**
     * 订单id，单笔支付必传
     */
    @ApiModelProperty("订单号，若单笔支付必传")
    private String tid;

    /**
     * 父订单号，用于合并支付场景，合并支付必传
     */
    @ApiModelProperty("父订单号，合并支付必传")
    private String parentTid;

    /**
     * 微信JSApi支付时必传
     */
    private String openid;

    private String paidCardRuleId;

    @Override
    public void checkParam() {
        Validate.isTrue( StringUtils.isNotEmpty(paidCardRuleId)  ||StringUtils.isNotEmpty(tid) || StringUtils.isNotEmpty(parentTid), NULL_EX_MESSAGE,
                "tid | parentTid");
    }
}
