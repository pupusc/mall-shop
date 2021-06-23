package com.wanmi.sbc.goods.flashsalegoods.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.flashsalecate.model.root.FlashSaleCate;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>抢购商品表实体类</p>
 *
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@Data
@Entity
@Table(name = "flash_sale_goods")
public class FlashSaleGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 活动日期：2019-06-11
     */
    @Column(name = "activity_date")
    private String activityDate;

    /**
     * 活动时间：13:00
     */
    @Column(name = "activity_time")
    private String activityTime;

    /**
     * skuID
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * spuID
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 抢购价
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 上限数量
     */
    @Column(name = "stock")
    private Integer stock;

    /**
     * 抢购销量
     */
    @Column(name = "sales_volume")
    private Long salesVolume;

    /**
     * 分类ID
     */
    @Column(name = "cate_id")
    private Long cateId;

    /**
     * 限购数量
     */
    @Column(name = "max_num")
    private Integer maxNum;

    /**
     * 起售数量
     */
    @Column(name = "min_num")
    private Integer minNum;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 包邮标志，0：不包邮 1:包邮
     */
    @Column(name = "postage")
    private Integer postage;

    /**
     * 删除标志，0:未删除 1:已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 活动日期+时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "activity_full_time")
    private LocalDateTime activityFullTime;

    /**
     * 分类信息
     */
    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "cate_id", insertable = false, updatable = false)
    private FlashSaleCate flashSaleCate;

    /**
     * SPU信息
     */
    @OneToOne
    @JoinColumn(name = "goods_id", insertable = false, updatable = false)
    private Goods goods;

    /**
     * SKU信息
     */
    @OneToOne
    @JoinColumn(name = "goods_info_id", insertable = false, updatable = false)
    private GoodsInfo goodsInfo;

    /**
     * 规格信息
     */
    @OneToMany
    @Where(clause = "del_flag = 0")
    @JoinColumn(name = "goods_info_id", insertable = false, updatable = false)
    private List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRelList;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     * 更新时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Column(name = "update_person")
    private String updatePerson;

}