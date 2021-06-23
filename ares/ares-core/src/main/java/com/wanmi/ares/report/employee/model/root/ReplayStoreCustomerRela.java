package com.wanmi.ares.report.employee.model.root;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
public class ReplayStoreCustomerRela implements Serializable {
    /**
     * 主键UUID
     */
    private String id;

    /**
     * 会员标识
     */
    private String customerId;

    /**
     * 店铺标识
     */
    private Long storeId;

    /**
     * 商家标识
     */
    private Integer companyInfoId;

    /**
     * 客户等级标识
     */
    private Long storeLevelId;

    /**
     * 负责业务员标识
     */
    private String employeeId;

    /**
     * 客户类型,0:店铺关联的客户,1:店铺发展的客户
     */
    private Integer customerType;

    /**
     * 创建时间
     */
    private Date createTime;

    private Long total;

}