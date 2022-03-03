package com.wanmi.sbc.order.trade.model.entity.value;

import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Administrator on 2017/5/1.
 */
@Data
public class Buyer implements Serializable {

    private static final long serialVersionUID = -4245870584779442939L;
    /**
     * 购买人编号
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
     * 标识用户是否属于当前订单所属商家,true|false
     */
    private boolean customerFlag = true;

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

    private String openId;

    /**
     * @param customer
     * @param level
     * @return
     */
    public static Buyer fromCustomer(CustomerSimplifyOrderCommitVO customer, Optional<CommonLevelVO> level, boolean customerFlag) {
        Buyer b = new Buyer();
        b.setId(customer.getCustomerId());
        b.setName(customer.getCustomerDetail().getCustomerName());
        b.setLevelId(customer.getCustomerLevelId());
        b.setPhone(customer.getCustomerDetail().getContactPhone());
        b.setAccount(customer.getCustomerAccount());
        b.setEmployeeId(customer.getCustomerDetail().getEmployeeId());
        b.setIepCustomer(customer.getEnterpriseCheckState() != null && customer.getEnterpriseCheckState() == EnterpriseCheckState.CHECKED);
        if (level.isPresent()) {
            CommonLevelVO commonLevelVO = level.get();
            b.setLevelName(commonLevelVO.getLevelName());
        }
        b.setCustomerFlag(customerFlag);
        b.setOpenId(customer.getOpenId());
        return b;
    }

    public static Buyer fromCustomer(CustomerVO customer) {
        Buyer b = new Buyer();
        b.setId(customer.getCustomerId());
        b.setName(customer.getCustomerDetail().getCustomerName());
        b.setLevelId(customer.getCustomerLevelId());
        b.setPhone(customer.getCustomerDetail().getContactPhone());
        b.setAccount(customer.getCustomerAccount());
        b.setEmployeeId(customer.getCustomerDetail().getEmployeeId());
        b.setIepCustomer(customer.getEnterpriseCheckState() != null && customer.getEnterpriseCheckState() == EnterpriseCheckState.CHECKED);
        return b;
    }
}
