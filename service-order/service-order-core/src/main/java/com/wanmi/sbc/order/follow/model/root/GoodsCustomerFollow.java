package com.wanmi.sbc.order.follow.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.enums.FollowFlag;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品客户收藏实体类
 * Created by dyt on 2017/5/17.
 */
@Data
@Entity
@Table(name = "goods_customer_follow")
public class GoodsCustomerFollow implements Serializable {

    /**
     * 图片编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    /**
     * 客户编号
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 商品编号
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * SKU编号
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 全局购买数
     */
    @Column(name = "goods_num")
    private Long goodsNum;

    /**
     * 公司信息ID
     */
    @Column(name = "company_info_id")
    private Long companyInfoId;

    /**
     * 采购创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;


    /**
     * 收藏标记
     */
    @Column(name = "follow_flag")
    @Enumerated
    private FollowFlag followFlag;

    /**
     * 收藏时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "follow_time")
    private LocalDateTime followTime;

    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 商品一级类目
     */
    @Column(name = "cate_top_id")
    private Long cateTopId;

    /**
     * 商品类目
     */
    @Column(name = "cate_id")
    private Long cateId;

    /**
     * 商品品牌
     */
    @Column(name = "brand_id")
    private Long brandId;

    /**
     * 终端来源
     */
    @Column(name = "terminal_source")
    private TerminalSource terminalSource;
}
