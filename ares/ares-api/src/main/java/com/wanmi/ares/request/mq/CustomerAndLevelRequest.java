package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import com.wanmi.ares.enums.CustomerType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 客户和等级关系表
 * Created by bail on 2018/01/11.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CustomerAndLevelRequest extends BaseMqRequest {

    private static final long serialVersionUID = 1791727470875246381L;

    /**
     * 用户标识
     */
    private String customerId;

    /**
     * 店铺标识
     */
    private String storeId;

    /**
     * 商家标识
     */
    private String companyInfoId;

    /**
     * 客户等级标识
     */
    private Long customerLevelId;

    /**
     * 负责的业务员标识
     */
    private String employeeId;

    /**
     * 关系类型(0:平台绑定客户,1:商家客户)
     */
    private CustomerType customerType;
}
