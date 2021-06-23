package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发票信息
 * Created by jinwei on 20/3/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class InvoiceVO implements Serializable {

    private static final long serialVersionUID = 1080590997967097805L;
    /**
     * 订单开票id
     */
    @ApiModelProperty(value = "订单开票id")
    private String orderInvoiceId;

    /**
     * 类型 0：普通发票 1：增值税发票 -1：无
     */
    @ApiModelProperty(value = "类型",dataType = "com.wanmi.sbc.account.bean.enums.InvoiceType")
    private Integer type;

    /**
     * 普通发票与增票至少一项必传
     */
    @ApiModelProperty(value = "普通发票与增票至少一项必传")
    private GeneralInvoiceVO generalInvoice;

    /**
     * 增值税发票与普票至少一项必传
     */
    @ApiModelProperty(value = "增值税发票与普票至少一项必传")
    private SpecialInvoiceVO specialInvoice;

    /**
     * 收货地址ID
     */
    @ApiModelProperty(value = "收货地址ID")
    private String addressId;

    /**
     * 是否单独的收货地址
     */
    @ApiModelProperty(value = "是否单独的收货地址")
    private boolean sperator;

    /**
     * 发票的收货地址
     */
    @ApiModelProperty(value = "发票的收货地址")
    private String address;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contacts;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String phone;

    /**
     * 收货地址的修改时间
     */
    @ApiModelProperty(value = "收货地址的修改时间")
    private String updateTime;

    /**
     * 开票项目id
     */
    @ApiModelProperty(value = "开票项目id")
    private String projectId;

    /**
     * 开票项目名称
     */
    @ApiModelProperty(value = "开票项目名称")
    private String projectName;

    /**
     * 开票项修改时间
     */
    @ApiModelProperty(value = "开票项修改时间")
    private String projectUpdateTime;

    /**
     * 省市区
     */
    @ApiModelProperty(value = "省")
    private Long provinceId;

    @ApiModelProperty(value = "市")
    private Long cityId;

    @ApiModelProperty(value = "区")
    private Long areaId;

    /**
     * 纳税人识别码
     */
    @ApiModelProperty(value = "纳税人识别码")
    private String taxNo;
}
