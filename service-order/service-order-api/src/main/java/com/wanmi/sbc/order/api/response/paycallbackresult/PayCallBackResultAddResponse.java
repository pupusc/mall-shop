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
 * <p>支付回调结果新增结果</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayCallBackResultAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的支付回调结果信息
     */
    @ApiModelProperty(value = "已新增的支付回调结果信息")
    private PayCallBackResultVO payCallBackResultVO;
}
