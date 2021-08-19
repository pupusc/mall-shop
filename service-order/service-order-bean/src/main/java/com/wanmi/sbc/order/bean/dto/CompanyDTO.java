package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.enums.BoolFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商家信息
 * Created by sunkun on 2017/11/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class CompanyDTO {

    /**
     * 商家主键
     */
    @ApiModelProperty(value = "商家主键")
    private Long companyInfoId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String companyCode;

    /**
     * 商家账号
     */
    @ApiModelProperty(value = "商家账号")
    private String accountName;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "是否是商家类型", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private BoolFlag companyType;
}
