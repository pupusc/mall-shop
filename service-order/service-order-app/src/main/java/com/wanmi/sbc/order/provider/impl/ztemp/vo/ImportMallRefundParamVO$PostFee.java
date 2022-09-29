package com.wanmi.sbc.order.provider.impl.ztemp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportMallRefundParamVO$PostFee {
    private List<ImportMallRefundParamVO$Detail> saleAfterRefundDetailBOList;
}
