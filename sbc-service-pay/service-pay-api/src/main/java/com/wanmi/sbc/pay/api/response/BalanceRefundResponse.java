package com.wanmi.sbc.pay.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName BalanceRefundResponse
 * @Description 余额支付在线退款返回数据对象
 * @Author lvzhenwei
 * @Date 2019/7/11 16:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceRefundResponse implements Serializable {

    private static final long serialVersionUID = 1855840229915201872L;

    //支付类型
    private String payType;
}
