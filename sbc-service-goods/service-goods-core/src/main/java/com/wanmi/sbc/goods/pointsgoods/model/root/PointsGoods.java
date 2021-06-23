package com.wanmi.sbc.goods.pointsgoods.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.pointsgoodscate.model.root.PointsGoodsCate;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>积分商品表实体类</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@Data
@Entity
@Table(name = "points_goods")
public class PointsGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分商品id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "points_goods_id")
    private String pointsGoodsId;

    /**
     * SpuId
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * SkuId
     */
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 分类id
     */
    @Column(name = "cate_id")
    private Integer cateId;

    /**
     * 字段更改，不代表积分商品库存，改为积分商品可兑换上限
     */
    @Column(name = "stock")
    private Long stock;

    /**
     * 销量
     */
    @Column(name = "sales")
    private Long sales;

    /**
     * 结算价格
     */
    @Column(name = "settlement_price")
    private BigDecimal settlementPrice;

    /**
     * 兑换积分
     */
    @Column(name = "points")
    private Long points;

    /**
     * 是否启用 0：停用，1：启用
     */
    @Column(name = "status")
    @Enumerated
    private EnableStatus status;

    /**
     * 推荐标价, 0: 未推荐 1: 已推荐
     */
    @Column(name = "recommend_flag")
    @Enumerated
    private BoolFlag recommendFlag;

    /**
     * 分类信息
     */
    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "cate_id", insertable = false, updatable = false)
    private PointsGoodsCate pointsGoodsCate;

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
     * 兑换开始时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    /**
     * 兑换结束时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "end_time")
    private LocalDateTime endTime;

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
     * 修改时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     * 删除标识,0: 未删除 1: 已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

}