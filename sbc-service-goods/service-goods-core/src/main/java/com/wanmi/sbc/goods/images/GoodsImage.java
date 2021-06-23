package com.wanmi.sbc.goods.images;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品图片实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "goods_image")
@NoArgsConstructor
@AllArgsConstructor
public class GoodsImage implements Serializable {

    /**
     * 图片编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

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
     * 原图路径
     */
    @Column(name = "artwork_url")
    private String artworkUrl;

    /**
     * 中图路径
     */
    @Column(name = "middle_url")
    private String middleUrl;

    /**
     * 小图路径
     */
    @Column(name = "thumb_url")
    private String thumbUrl;

    /**
     * 大图路径
     */
    @Column(name = "big_url")
    private String bigUrl;

    /**
     * 图片排序字段
     */
    @Column(name = "sort")
    private Integer sort;

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
     * 删除标记
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

}
