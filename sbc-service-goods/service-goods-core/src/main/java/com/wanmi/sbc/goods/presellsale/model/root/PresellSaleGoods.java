package com.wanmi.sbc.goods.presellsale.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预售活动商品关联表
 */
@Entity
@Table(name = "presell_sale_goods")
@Data
public class PresellSaleGoods {

    /**
     * 预售商品活动id，采用UUID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;


    /**
     * 预售活动id
     */
    @Column(name = "presell_sale_id")
    private String presellSaleId;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 商品skuID
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;


    /**
     * 商品spuId
     */
    @Column(name = "goods_id")
    private String goodsId;


    /**
     * 预售商品定金金额
     */
    @Column(name = "handsel_price")
    private BigDecimal handselPrice;


    /**
     * 预售商品定金膨胀金额
     */
    @Column(name = "inflation_price")
    private BigDecimal inflationPrice;


    /**
     * 预售价格，只有预售类型为全款的有值
     */
    @Column(name = "presell_price")
    private BigDecimal presellPrice;

    /**
     * 预售商品限购数量
     */
    @Column(name = "presell_sale_count")
    private Integer presellSaleCount;

    /**
     * 支付定金人数
     */
    @Column(name = "handsel_num")
    private Integer handselNum;


    /**
     * 支付尾款人数
     */
    @Column(name = "final_payment_num")
    private Integer finalPaymentNum;


    /**
     * 全款支付人数
     */
    @Column(name = "full_payment_num")
    private Integer fullPaymentNum;


    /**
     * 活动创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;


    /**
     * 活动修改时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;


    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String create_person;


    /**
     * 跟新人
     */
    @Column(name = "update_person")
    private String update_person;



    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
