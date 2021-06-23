package com.wanmi.sbc.customer.store.model.entity;

import com.wanmi.sbc.common.enums.BoolFlag;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:19 2019/5/23
 * @Description:
 */
@Data
@NoArgsConstructor
public class StoreName {

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
    @Enumerated
    private BoolFlag companyType;

    public StoreName(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public StoreName(Long storeId, BoolFlag companyType) {
        this.storeId = storeId;
        this.companyType = companyType;
    }

    public StoreName(Long storeId, String storeName, BoolFlag companyType) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.companyType = companyType;
    }
}
