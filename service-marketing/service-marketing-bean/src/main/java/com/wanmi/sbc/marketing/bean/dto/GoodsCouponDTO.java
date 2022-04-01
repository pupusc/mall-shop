package com.wanmi.sbc.marketing.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsCouponDTO implements Serializable {
    private static final long serialVersionUID = 4594267420799324537L;

    private String goodsId;

    private List<String> couponCodes;
}