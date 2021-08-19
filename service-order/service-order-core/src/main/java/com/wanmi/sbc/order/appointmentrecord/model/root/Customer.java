package com.wanmi.sbc.order.appointmentrecord.model.root;

import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import lombok.Data;

import java.io.Serializable;


@Data
public class Customer implements Serializable {


    private static final long serialVersionUID = -3602160639701934746L;
    /**
     * 预约人编号
     */
    private String id;

    /**
     * 购买人姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 等级编号
     */
    private Long levelId;

    /**
     * 等级名称
     */
    private String levelName;

    /**
     *
     */
    private String phone;

    /**
     * 买家关联的业务员id
     */
    private String employeeId;

    /**
     * 是否企业会员
     */
    private boolean isIepCustomer;


    /**
     * @param customerVo
     * @return
     */
    public static Customer fromCustomer(CustomerVO customerVo) {
        Customer customer = new Customer();
        customer.setId(customerVo.getCustomerId());
        customer.setName(customerVo.getCustomerDetail().getCustomerName());
        customer.setLevelId(customerVo.getCustomerLevelId());
        customer.setPhone(customerVo.getCustomerDetail().getContactPhone());
        customer.setAccount(customerVo.getCustomerAccount());
        customer.setEmployeeId(customerVo.getCustomerDetail().getEmployeeId());
        customer.setIepCustomer(customerVo.getEnterpriseCheckState() != null && customerVo.getEnterpriseCheckState() == EnterpriseCheckState.CHECKED);

        return customer;
    }

}
