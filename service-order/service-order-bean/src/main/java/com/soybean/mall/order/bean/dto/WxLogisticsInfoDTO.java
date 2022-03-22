package com.soybean.mall.order.bean.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxLogisticsInfoDTO implements Serializable {
    private static final long serialVersionUID = -8067580126626675257L;
    /**
    管易物流公司code
     */
    private String erpLogisticCode;

    private String logisticName;

    private String logisticCode;
}
