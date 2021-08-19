package com.wanmi.sbc.order.api.response.refund;

import com.wanmi.sbc.order.bean.vo.RefundBillVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 退款单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundBillResponse extends RefundBillVO implements Serializable {

    private static final long serialVersionUID = -1345913335962267570L;
}
