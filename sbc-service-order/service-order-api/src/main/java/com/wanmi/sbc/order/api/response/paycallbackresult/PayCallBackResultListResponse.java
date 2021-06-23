package com.wanmi.sbc.order.api.response.paycallbackresult;

import com.wanmi.sbc.order.bean.vo.PayCallBackResultVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>支付回调结果列表结果</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayCallBackResultListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 支付回调结果列表结果
     */
    @ApiModelProperty(value = "支付回调结果列表结果")
    private List<PayCallBackResultVO> payCallBackResultVOList;
}
