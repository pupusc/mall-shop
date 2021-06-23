package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 员工
 * Created by sunkun on 2017/9/21.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class EmployeeRequest extends BaseMqRequest {

    private static final long serialVersionUID = 7896020625040862634L;

    /**
     * 是否业务员 0:是业务员 1:不是业务员
     */
    private Integer isEmployee;

    /**
     * 名称
     */
    private String name;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 商家id
     */
    private String companyId;

}
