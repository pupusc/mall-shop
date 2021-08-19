package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author : baijz
 * @Date : 2019/3/1 11 38
 * @Description : 门店信息——分销记录
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreSimpleInfo implements Serializable {

    private static final long serialVersionUID = -8576096693233067338L;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String companyCode;
}
