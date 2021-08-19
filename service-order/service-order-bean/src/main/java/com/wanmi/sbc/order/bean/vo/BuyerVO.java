package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Administrator on 2017/5/1.
 */
@Data
@ApiModel
public class BuyerVO implements Serializable {

    private static final long serialVersionUID = -4245870584779442939L;

    /**
     * 购买人编号
     */
    @ApiModelProperty(value = "购买人编号")
    private String id;

    /**
     * 购买人姓名
     */
    @ApiModelProperty(value = "购买人姓名")
    private String name;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String account;

    /**
     * 等级编号
     */
    @ApiModelProperty(value = "等级编号")
    private Long levelId;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;

    /**
     * 标识用户是否属于当前订单所属商家,true|false
     */
    @ApiModelProperty(value = "标识用户是否属于当前订单所属商家",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean customerFlag = true;

    /**
     *
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 买家关联的业务员id
     */
    @ApiModelProperty(value = "买家关联的业务员id")
    private String employeeId;

    /**
     * @param customer
     * @param level
     * @return
     */
    public static BuyerVO fromCustomer(CustomerVO customer, Optional<CustomerLevelVO> level, boolean customerFlag) {
        BuyerVO b = new BuyerVO();
        b.setId(customer.getCustomerId());
        b.setName(customer.getCustomerDetail().getCustomerName());
        b.setLevelId(customer.getCustomerLevelId());
        b.setPhone(customer.getCustomerDetail().getContactPhone());
        b.setAccount(customer.getCustomerAccount());
        b.setEmployeeId(customer.getCustomerDetail().getEmployeeId());
        if (level.isPresent()) {
            CustomerLevelVO customerLevel = level.get();
            b.setLevelName(customerLevel.getCustomerLevelName());
        }
        b.setCustomerFlag(customerFlag);
        return b;
    }
}
