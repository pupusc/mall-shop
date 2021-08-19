package com.wanmi.sbc.elastic.settlement.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.SettleStatus;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author yangzhen
 * @Description //结算单
 * @Date 10:21 2020/12/14
 * @Param
 * @return
 **/
@Document(indexName = EsConstants.DOC_SETTLEMENT, type = EsConstants.DOC_SETTLEMENT)
@Data
public class EsSettlement implements Serializable {

    private static final long serialVersionUID = 4555211803309442026L;

    @Id
    private String id;

    /**
     * 用于生成结算单号，结算单号自增
     */
    @Field(type = FieldType.Long)
    private Long settleId;


    /**
     * 结算单创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;



    /**
     * 结算单号
     */
    @Field(type = FieldType.Text)
    private String settleCode;

    /**
     * 结算单开始时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结算单结束时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 结算单结算时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime settleTime;

    /**
     * 商家Id
     */
    @Field(type = FieldType.Long)
    private Long storeId;

    /**
     * 商铺名称
     */
    @Field(type = FieldType.Keyword)
    private String storeName;


    /**
     * 交易总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal salePrice;

    /**
     * 商品销售总数
     */
    @Field(type = FieldType.Long)
    private long saleNum;

    /**
     * 退款总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal returnPrice;

    /**
     * 商品退货总数
     */
    @Field(type = FieldType.Long)
    private long returnNum;

    /**
     * 平台佣金总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal platformPrice;

    /**
     * 店铺应收
     */
    @Field(type = FieldType.Double)
    private BigDecimal storePrice;

    /**
     * 商品供货总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal providerPrice;

    /**
     * 总运费
     */
    @Field(type = FieldType.Double)
    private BigDecimal deliveryPrice;

    /**
     * 商品实付总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal splitPayPrice;

    /**
     * 通用券优惠总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal commonCouponPrice;

    /**
     * 积分抵扣总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal pointPrice;

    /**
     * 分销佣金总额
     */
    @Field(type = FieldType.Double)
    private BigDecimal commissionPrice;

    /**
     * 结算状态
     */
    @Field(type = FieldType.Integer)
    private SettleStatus settleStatus;

    /**
     * 商家类型
     */
    @Field(type = FieldType.Integer)
    private StoreType storeType;

}
