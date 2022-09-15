package com.wanmi.sbc.order.provider.impl.ztemp.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class ImportMallRefundParamVO {
    /**
     * 业务发起
     */
    @NotBlank
    private String saleAfterCreateEnum;
    /**
     * 商城订单id
     */
    @NotBlank
    private String mallOrderId;
    /**
     * 退款类型
     */
    @NotEmpty
    private List<Integer> refundTypeList = new ArrayList<>();
    /**
     * 售后主单
     */
    @NotNull
    private ImportMallRefundParamVO$Order saleAfterOrderBO;
    /**
     * 快递费
     */
    private ImportMallRefundParamVO$PostFee saleAfterPostFee;
    /**
     * 售后子单
     */
    @NotEmpty
    private List<ImportMallRefundParamVO$Item> saleAfterItemBOList = new ArrayList<>();
    /**
     * 退款主单
     */
    private List<ImportMallRefundParamVO$Refund> saleAfterRefundBOList = new ArrayList<>();
}
