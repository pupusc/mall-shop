package com.wanmi.sbc.customer.api.request.detail;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 会员修改请求
 * Created by CHENLI on 2017/4/20.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetailAddRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 722270859764500264L;

    /**
     * 会员详细信息标识UUID
     */
    @ApiModelProperty(value = "会员详细信息标识UUID")
    private String customerDetailId;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String customerAddress;

    /**
     * 联系人名字
     */
    @ApiModelProperty(value = "联系人名字")
    private String contactName;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    /**
     * 负责业务员
     */
    @ApiModelProperty(value = "负责业务员")
    private String employeeId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @NotNull
    private String createPerson;
}
