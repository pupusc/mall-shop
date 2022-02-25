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
public class WxProductInfoDTO implements Serializable {
    private static final long serialVersionUID = -8775193614970574093L;
    @JSONField(name ="out_product_id")
    private String outProductId;
    @JSONField(name ="out_sku_id")
    private String outSkuId;
    @JSONField(name ="product_cnt")
    private Long productNum;
    @JSONField(name ="sale_price")
    private BigDecimal salePrice;
    @JSONField(name ="real_price")
    private BigDecimal realPrice;
    private String path;
    private String title;
    @JSONField(name ="head_img")
    private String headImg;
}
