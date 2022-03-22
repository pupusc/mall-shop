package com.soybean.mall.wx.mini.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxOrderDetailDTO implements Serializable {
    private static final long serialVersionUID = -2840521021603354162L;
    @JSONField(name ="product_infos")
    private List<WxProductInfoDTO> productInfos;
    @JSONField(name ="pay_info")
    private WxPayInfoDTO payInfo;
    @JSONField(name ="price_info")
    private WxPriceInfoDTO priceInfo;
}
