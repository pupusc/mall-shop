package com.wanmi.sbc.order.payorder.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 操作传参
 * Created by zhangjin on 2017/5/8.
 */
@Data
public class PayOrderOperateRequest implements Serializable{
    /**
     * 收款单主键
     */
    private List<String> payOrderIds;
}
