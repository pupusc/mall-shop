package com.wanmi.sbc.order.appointmentrecord.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商家名称
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier implements Serializable{

    private static final long serialVersionUID = 3541731840944835830L;

    /**
     * 商家编码
     */
    private String supplierCode;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 商家id
     */
    private Long supplierId;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 使用的运费模板类别(0:店铺运费,1:单品运费)
     */
    private DefaultFlag freightTemplateType;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 代理人Id，用于代客下单
     */
    private String employeeId;

    /**
     * 代理人名称，用于代客下单，相当于OptUserName
     */
    private String employeeName;

    /**
     * 是否平台自营
     */
    private Boolean isSelf;
}
