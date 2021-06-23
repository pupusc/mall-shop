package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@ApiModel
@Data
public class YzOrderCustomerVO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "orderNo")
    private String orderNo;

    /**
     * yz_uid
     */
    @ApiModelProperty(value = "yzUid")
    private Long yzUid;

    /**
     * yz_openId
     */
    @ApiModelProperty(value = "yzOpenId")
    private String yzOpenId;

    /**
     * flag(是否补偿完成0：未开始 1：已完成)
     */
    @ApiModelProperty(value = "flag")
    private Integer flag;

}
