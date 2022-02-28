package com.soybean.mall.wx.mini.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxPriceInfoDTO implements Serializable {
    private static final long serialVersionUID = -7952026075135048783L;
    @JSONField(name ="order_price")
    private Integer orderPrice;
    private Integer freight;
    @JSONField(name ="discounted_price")
    private Integer discountePrice;
}
