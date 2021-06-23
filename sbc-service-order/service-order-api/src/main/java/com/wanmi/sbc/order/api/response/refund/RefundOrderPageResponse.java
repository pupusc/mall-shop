package com.wanmi.sbc.order.api.response.refund;

import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.order.bean.vo.RefundOrderResponse;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页返回
 * Created by zhangjin on 2017/4/30.
 */
@Data
@ApiModel
public class RefundOrderPageResponse extends BaseQueryResponse<RefundOrderResponse> implements Serializable {
}
