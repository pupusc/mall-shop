package com.soybean.mall.wx.mini.order.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.INTERNAL;

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
    private Integer salePrice;
    @JSONField(name ="real_price")
    private Integer realPrice;

    /**
     * 新加字段，重要参数，验证价格时使用
     */
    @JSONField(name ="sku_real_price")
    private Integer skuRealPrice;
    private String path;
    private String title;
    @JSONField(name ="head_img")
    private String headImg;
}
