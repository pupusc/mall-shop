package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 客户信息基类-只包含客户的基本信息
 * Created by bail on 2017/10/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CustomerBaseRequest extends BaseMqRequest {

    private static final long serialVersionUID = 3881537593024134920L;

    /**
     * 会员登录账号|手机号
     */
    private String account;

    /**
     * 客户级别
     */
    private Long levelId;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 市id
     */
    private Long cityId;

    /**
     * 员工信息ID
     */
    private String employeeId;

    /**
     * 商家id
     */
    private String companyId;

}
