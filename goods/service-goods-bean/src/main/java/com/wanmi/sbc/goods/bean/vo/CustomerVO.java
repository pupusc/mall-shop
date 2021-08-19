package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class CustomerVO implements Serializable {


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


}
