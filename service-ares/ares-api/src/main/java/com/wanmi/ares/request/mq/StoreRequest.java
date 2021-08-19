package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import com.wanmi.ares.enums.StoreState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * 店铺
 * Created by bail on 2018/01/11.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class StoreRequest extends BaseMqRequest {

    private static final long serialVersionUID = 8386464364127088917L;

    /**
     * 商家Id
     */
    private String companyInfoId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 店铺状态 0、开启 1、关店
     */
    private StoreState storeState;

    /**
     * 签约开始日期
     */
    private LocalDate contractStartDate;

    /**
     * 签约结束日期
     */
    private LocalDate contractEndDate;
}
