//package com.sbc.wanmi.erp.bean.vo;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//
///**
// * @program: sbc-background
// * @description: 退货单商品对象
// * @author: 0F3685-wugongjiang
// * @create: 2021-02-07 16:41
// **/
//@ApiModel
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ReturnTradeItemVO implements Serializable {
//
//    /**
//     *商品代码(商品条码与商品代码二者必选其一)
//     */
//    @ApiModelProperty(value = "商品代码")
//    private String spuCode;
//
//    /**
//     *规格代码(带规格的商品此字段必填)
//     */
//    @ApiModelProperty(value = "规格代码")
//    private String skuCode;
//
//    /**
//     * 退货数量
//     */
//    @ApiModelProperty(value = "退货数量")
//    private int qty;
//}
