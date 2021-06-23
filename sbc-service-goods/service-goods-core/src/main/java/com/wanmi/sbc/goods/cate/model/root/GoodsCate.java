package com.wanmi.sbc.goods.cate.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品分类实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "goods_cate")
public class GoodsCate implements Serializable {

    /**
     * 分类编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private Long cateId;

    /**
     * 分类名称
     */
    @NotBlank
    @Length(min = 1, max = 20)
    @Column(name = "cate_name")
    private String cateName;

    /**
     * 父类编号
     */
    @Column(name = "cate_parent_id")
    private Long cateParentId;

    /**
     * 分类图片
     */
    @Column(name = "cate_img")
    private String cateImg;

    /**
     * 分类路径
     */
    @Column(name = "cate_path")
    private String catePath;

    /**
     * 分类层次
     */
    @Column(name = "cate_grade")
    private Integer cateGrade;

    /**
     * 分类扣率
     */
    @Column(name = "cate_rate")
    private BigDecimal cateRate;

    /**
     * 是否使用上级类目扣率 0 否   1 是
     */
    @Column(name = "is_parent_cate_rate")
    private DefaultFlag isParentCateRate;

    /**
     * 成长值获取比例
     */
    @Column(name = "growth_value_rate")
    private BigDecimal growthValueRate;

    /**
     * 是否使用上级类目成长值获取比例 0 否   1 是
     */
    @Column(name = "is_parent_growth_value_rate")
    private DefaultFlag isParentGrowthValueRate;

    /**
     * 积分获取比例
     */
    @Column(name = "points_rate")
    private BigDecimal pointsRate;

    /**
     * 是否使用上级类目积分获取比例 0 否   1 是
     */
    @Column(name = "is_parent_points_rate")
    private DefaultFlag isParentPointsRate;

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

    /**
     * 默认标记
     */
    @Column(name = "is_default")
    @Enumerated
    private DefaultFlag isDefault;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 一对多关系，子分类
     */
    @Transient
    private List<GoodsCate> goodsCateList;

    @Transient
    @OneToMany(mappedBy = "goodsCate")
    private List<GoodsProp> goodsProps;
}
