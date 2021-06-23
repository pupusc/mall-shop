package com.wanmi.sbc.account.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>对账单返回数据结构</p>
 * Created by of628-wenzhi on 2017-12-08-上午11:16.
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRecordVO implements Serializable {


    private static final long serialVersionUID = -6716319671270299759L;

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
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long supplierId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 总计金额，单位元，格式："￥#,###.00"
     */
    @ApiModelProperty(value = "总计金额")
    private String totalAmount;

    /**
     * 存在各支付项金额的Map，key: 枚举PayWay的name, value: 金额，单位元，格式："￥#,###.00"
     */
    @ApiModelProperty(value = "存在各支付项金额的Map(key: 枚举PayWay的name, value: 金额，单位元)")
    private Map<String, String> payItemAmountMap;
}
