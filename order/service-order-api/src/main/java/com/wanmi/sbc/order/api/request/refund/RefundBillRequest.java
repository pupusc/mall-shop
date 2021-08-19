package com.wanmi.sbc.order.api.request.refund;

import com.wanmi.sbc.order.bean.dto.RefundBillDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 流水单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@ApiModel
public class RefundBillRequest extends RefundBillDTO implements Serializable{

    private static final long serialVersionUID = -5819780428877519731L;
}
