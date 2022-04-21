package com.wanmi.sbc.order.api.request.returnorder;

import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据动态条件查询退单列表请求结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnOrderByConditionRequest implements Serializable {

    private static final long serialVersionUID = 4079600894275807085L;

    @ApiModelProperty(value = "退单编号")
    private String rid;

    @ApiModelProperty(value = "订单编号")
    private String tid;

    @ApiModelProperty(value = "订单编号List")
    private List<String> tids;

    /**
     * 尾款退单号
     */
    @ApiModelProperty(value = "尾款退单号")
    private String businessTailId;

    /**
     * 购买人编号
     */
    @ApiModelProperty(value = "购买人id")
    private String buyerId;

    @ApiModelProperty(value = "购买人姓名")
    private String buyerName;

    @ApiModelProperty(value = "购买人账户")
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

    /**
     * 供应商公司id
     */
    @ApiModelProperty(value = "供应商公司id")
    private Long providerCompanyInfoId;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String providerCode;

    /**
     * 视频号售后id
     */
    private String aftersaleId;
}
