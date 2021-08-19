package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 电子发票信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceInfo implements Serializable {

    private static final long serialVersionUID = 7018587123159056755L;

    /**
     * 买家税号
     */
    private String taxpayer_id;

    /**
     * 抬头类型:personal 个人,enterprise 企业
     */
    private String raise_type;

    /**
     * 抬头
     */
    private String user_name;

    /**
     * 发票详情类型:itemCategory 商品类别,itemDetail 商品明细
     */
    private String invoice_detail_type;

    /**
     * 买家邮箱
     */
    private String email;
}
