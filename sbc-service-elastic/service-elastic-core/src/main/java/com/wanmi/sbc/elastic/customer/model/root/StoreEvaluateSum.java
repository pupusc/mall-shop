package com.wanmi.sbc.elastic.customer.model.root;

import com.wanmi.sbc.common.util.EsConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Document(indexName = EsConstants.STORE_EVALUATE_SUM, type = EsConstants.STORE_EVALUATE_SUM)
public class StoreEvaluateSum implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * id 主键
     */
    @Id
    private Long sumId;

    /**
     * 店铺id
     */
    @Field(type = FieldType.Keyword)
    private Long storeId;

    /**
     * 店铺名称
     */
    @Field(type = FieldType.Keyword)
    private String storeName;

    /**
     * 服务综合评分
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal sumServerScore;

    /**
     * 商品质量综合评分
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal sumGoodsScore;

    /**
     * 物流综合评分
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal sumLogisticsScoreScore;

    /**
     * 订单数
     */
    @Field(type = FieldType.Keyword)
    private Integer orderNum;

    /**
     * 评分周期 0：30天，1：90天，2：180天
     */
    @Field(type = FieldType.Keyword)
    private Integer scoreCycle;

    /**
     * 综合评分
     */
    @Field(type = FieldType.Keyword)
    private BigDecimal sumCompositeScore;

}