package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据状态统计退单请求结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnOrderCountByFlowStateRequest implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    @ApiModelProperty(value = "退单id")
    private String rid;

    @ApiModelProperty(value = "订单id")
    private String tid;

    /**
     * 购买人编号
     */
    @ApiModelProperty(value = "购买人编号")
    private String buyerId;

    @ApiModelProperty(value = "购买人姓名")
    private String buyerName;

    @ApiModelProperty(value = "购买人账号")
    private String buyerAccount;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String consigneeName;

    @ApiModelProperty(value = "手机号")
    private String consigneePhone;

    /**
     * 退单流程状态
     */
    @NotNull
    @ApiModelProperty(value = "退单流程状态")
    private ReturnFlowState returnFlowState;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String skuName;

    @ApiModelProperty(value = "sku编号")
    private String skuNo;

    /**
     * 退单编号列表
     */
    @ApiModelProperty(value = "退单编号列表")
    private String[] rids;

    /**
     * 退单创建开始时间，精确到天
     */
    @ApiModelProperty(value = "退单创建开始时间，精确到天")
    private String beginTime;

    /**
     * 退单创建结束时间，精确到天
     */
    @ApiModelProperty(value = "退单创建结束时间，精确到天")
    private String endTime;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String supplierCode;

    /**
     * 业务员id
     */
    @ApiModelProperty(value = "业务员id")
    private String employeeId;

    /**
     * 客户id
     */
    @ApiModelProperty(value = "客户id")
    private Object[] customerIds;

    /**
     * pc搜索条件
     */
    @ApiModelProperty(value = "pc搜索条件")
    private String tradeOrSkuName;

}
