package com.wanmi.sbc.order.api.request.refund;


import com.wanmi.sbc.order.bean.dto.RefundOrderRequestDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;


/**
 * 退款单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundOrderWithoutPageRequest extends RefundOrderRequestDTO {

    private static final long serialVersionUID = -5620693206009388482L;
}
