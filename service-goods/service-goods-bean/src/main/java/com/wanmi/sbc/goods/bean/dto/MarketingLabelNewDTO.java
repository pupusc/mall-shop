package com.wanmi.sbc.goods.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
public class MarketingLabelNewDTO implements Serializable {

    private String name;

    private BigDecimal fix_price;

    private Integer sale_num;

    private List<Labels> labels;

    @Data
    public static class Labels{
        private String sku_no;
        private String spu_no;
        private String show_name;
        private String name;
        private String goods_info_id;
        private String goods_info_name;
        private Integer id;
        private Integer order_num;
        private Integer order_type;
        private Integer freight_temp_id;
    }

}
