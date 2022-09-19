package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;


@Data
public class OrderDevItemReq implements Serializable {

    /**
     * 子订单id
     */
    private Long orderItemId;


}
