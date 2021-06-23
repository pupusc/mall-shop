package com.wanmi.sbc.customer.storecustomer.root;

import com.wanmi.sbc.customer.bean.enums.CustomerType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 店铺-会员(包含会员等级)关联实体类
 * Created by bail on 2017/11/13.
 */
@Data
@Entity
@Table(name = "store_customer_rela")
public class StoreCustomerRela implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * 用户标识
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 店铺标识
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 商家标识
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 店铺等级标识
     */
    @Column(name = "store_level_id")
    private Long storeLevelId;

    /**
     * 负责的业务员标识
     */
    @Column(name = "employee_id")
    private String employeeId;

    /**
     * 关系类型(0:店铺关联的客户,1:店铺发展的客户)
     */
    @Column(name = "customer_type")
    @Enumerated
    private CustomerType customerType;

}

