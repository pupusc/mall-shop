package com.wanmi.sbc.goods.api.request.appointmentsale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName RushToAppointmentSaleGoodsRequest
 * @Description 预约活动Request请求类
 * @Author zhangxiaodong
 * @Date 2019/6/14 9:38
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RushToAppointmentSaleGoodsRequest implements Serializable {


    private static final long serialVersionUID = 4930391273154325186L;
    /**
     * 预约活动Id
     */
    @ApiModelProperty(value = "预约活动Id")
    @NotNull
    private Long appointmentSaleId;


    /**
     * 预约商品skuId
     */
    @ApiModelProperty(value = "预约商品skuId")
    @NotBlank
    private String skuId;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    private String customerId;

    /**
     * 抢购数量
     */
    @ApiModelProperty(value = "抢购数量")
    private Long num;
}
