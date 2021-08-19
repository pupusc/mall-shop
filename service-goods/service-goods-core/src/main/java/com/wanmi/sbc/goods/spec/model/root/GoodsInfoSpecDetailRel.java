package com.wanmi.sbc.goods.spec.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SKU规格值关联实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "goods_info_spec_detail_rel")
public class GoodsInfoSpecDetailRel implements Serializable {

    /**
     * SKU与规格值关联ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spec_detail_rel_id")
    private Long specDetailRelId;

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
     * 规格值ID
     */
    @Column(name = "spec_detail_id")
    private Long specDetailId;

    /**
     * 规格ID
     */
    @Column(name = "spec_id")
    private Long specId;

    /**
     * 规格值自定义名称
     * 分词搜索
     */
    @Column(name = "detail_name")
    private String detailName;

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
     * 是否删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 新增商品时，模拟规格ID
     * 表明与SKU的关系
     */
    @Transient
    private Long mockSpecId;

    /**
     * 新增商品时，模拟规格值ID
     * 表明与SKU的关系
     */
    @Transient
    private Long mockSpecDetailId;

    /**
     * 规格项名称
     */
    @Transient
    private String specName;

    /**
     * 规格项值
     */
    @Transient
    private String allDetailName;
}
