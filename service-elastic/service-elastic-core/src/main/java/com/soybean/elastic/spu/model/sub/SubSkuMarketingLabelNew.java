package com.soybean.elastic.spu.model.sub;


import com.soybean.elastic.collect.constant.ConstantUtil;
import com.wanmi.sbc.goods.bean.dto.MarketingLabelNewDTO;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
public class SubSkuMarketingLabelNew {

    @Field(type =FieldType.Text )
    private String name;

    @Field(type = FieldType.Nested)
    private List<Labels> labels;

    @Data
    public static class Labels{

        @Field(type =FieldType.Text )
        private String sku_no;

        @Field(type =FieldType.Text )
        private String spu_no;

        @Field(type = FieldType.Text, analyzer = ConstantUtil.ES_DEFAULT_ANALYZER, searchAnalyzer = ConstantUtil.ES_DEFAULT_SEARCH_ANALYZER)
        private String show_name;

        @Field(type =FieldType.Text )
        private String name;

        @Field(type =FieldType.Text )
        private String goods_info_id;

        @Field(type =FieldType.Text )
        private String goods_info_name;

        @Field(type =FieldType.Integer )
        private Integer id;

        @Field(type =FieldType.Integer )
        private Integer order_num;

        @Field(type =FieldType.Integer )
        private Integer order_type;

        @Field(type =FieldType.Integer )
        private Integer freight_temp_id;
    }

}
