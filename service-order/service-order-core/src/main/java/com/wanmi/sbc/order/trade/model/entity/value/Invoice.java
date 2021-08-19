package com.wanmi.sbc.order.trade.model.entity.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发票信息
 * Created by jinwei on 20/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice implements Serializable {

    /**
     * 订单开票id
     */
    private String orderInvoiceId;

    /**
     * 类型 0：普通发票 1：增值税发票 -1：无
     */
    private Integer type;

    /**
     * 普通发票与增票至少一项必传
     */
    private GeneralInvoice generalInvoice;

    /**
     * 增值税发票与普票至少一项必传
     */
    private SpecialInvoice specialInvoice;

    /**
     * 收货地址ID
     */
    private String addressId;

    /**
     * 是否单独的收货地址
     */
    private boolean sperator;

    /**
     * 发票的收货地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 收货地址的修改时间
     */
    private String updateTime;

    /**
     * 开票项目id
     */
    private String projectId;

    /**
     * 开票项目名称
     */
    private String projectName;

    /**
     * 开票项修改时间
     */
    private String projectUpdateTime;

    /**
     * 省市区
     */
    private Long provinceId;

    private Long cityId;

    private Long areaId;

    /**
     * 纳税人识别码
     */
    private String taxNo;
}
