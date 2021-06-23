package com.wanmi.sbc.order.trade.model.entity.value;

import com.wanmi.sbc.common.enums.BoolFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家信息
 * Created by sunkun on 2017/11/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    /**
     * 商家主键
     */
    private Long companyInfoId;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 商家编号
     */
    private String companyCode;

    /**
     * 商家账号
     */
    private String accountName;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    private BoolFlag companyType;
}
