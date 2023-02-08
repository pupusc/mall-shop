package com.wanmi.sbc.order.bean.vo;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class trade_order implements Serializable {

    private String trade_order_id;

    private String customer_id;

    private String customer_account;

    private String supplier_id;

    private String store_id;

    private String audit_state;

    private String flow_state;

    private String pay_state;

    private String deliver_status;

    private String create_time;

    private String oid;

    private String spu_id;

    private String sku_id;

    private String sku_name;

    private String sku_no;

    private int cate_id;

    private int brand_id;
}
