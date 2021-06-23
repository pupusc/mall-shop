package com.wanmi.sbc.order.api.response.paycallbackresult;

import com.wanmi.sbc.order.bean.vo.PayCallBackResultVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>支付回调结果修改结果</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayCallBackResultModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的支付回调结果信息
     */
    @ApiModelProperty(value = "已修改的支付回调结果信息")
    private PayCallBackResultVO payCallBackResultVO;
}
