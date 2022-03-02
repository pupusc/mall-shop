package com.soybean.mall.vo;

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
public class WxProductInfoVO implements Serializable {
    private static final long serialVersionUID = 4958268411494180474L;
    @JSONField(name ="out_product_id")
    private String outProductId;
    @JSONField(name ="out_sku_id")
    private String outSkuId;
    @JSONField(name ="product_cnt")
    private Long productNum;
    @JSONField(name ="sale_price")
    private Integer salePrice;
    @JSONField(name ="real_price")
    private Integer realPrice;
    private String path ="test";
    private String title;
    @JSONField(name ="head_img")
    private String headImg ="test.jpg";
}
