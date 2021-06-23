package com.wanmi.sbc.order.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 购买人参数
 */
@Data
@ApiModel
public class BuyerDTO implements Serializable {

    private static final long serialVersionUID = -6952082465485575874L;

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
    @ApiModelProperty(value = "标识用户是否属于当前订单所属商家", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private boolean customerFlag = true;

    /**
     *
     */
    @ApiModelProperty(value = "购买人联系电话")
    private String phone;

    /**
     * 买家关联的业务员id
     */
    @ApiModelProperty(value = "买家关联的业务员id")
    private String employeeId;

}
