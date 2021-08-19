package com.wanmi.sbc.goods.brand.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品品牌实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "goods_brand")
public class GoodsBrand implements Serializable {

    /**
     * 品牌编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long brandId;

    /**
     * 品牌名称
     */
    @Column(name = "brand_name")
    private String brandName;

    /**
     * 拼音
     */
    @Column(name = "pin_yin")
    @CanEmpty
    private String pinYin;

    /**
     * 简拼
     */
    @Column(name = "s_pin_yin")
    @CanEmpty
    private String sPinYin;

    /**
     * 店铺id(平台默认为0)
     */
    @Column(name = "store_id")
    private Long storeId = 0L;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 品牌别名
     */
    @Column(name = "brand_nick_name")
    @CanEmpty
    private String nickName;


    /**
     * 品牌logo
     */
    @Column(name = "brand_logo")
    @CanEmpty
    private String logo;
}
