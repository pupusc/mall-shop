package com.wanmi.sbc.goods.distributor.goods.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 分销员商品表
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:02
 * @version: 1.0
 */
@Data
@Entity
@Table(name = "distributor_goods_info")
public class DistributorGoodsInfo {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * 分销员对应的会员ID
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 分销商品SKU编号
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;


    /**
     * 分销商品SPU编号
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private Long storeId;


    /**
     * 分销商品顺序
     */
    @Column(name = "sequence")
    private Integer sequence;

    /**
     * 是否删除
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
}
