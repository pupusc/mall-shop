package com.wanmi.sbc.goods.standard.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.Api;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品库实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "standard_goods")
public class StandardGoods {

    /**
     * 商品编号，采用UUID
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 分类编号
     */
    @Column(name = "cate_id")
    private Long cateId;

    /**
     * 品牌编号
     */
    @Column(name = "brand_id")
    @CanEmpty
    private Long brandId;

    /**
     * SPU编码
     */
    @Column(name = "goods_no")
    private String goodsNo;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品副标题
     */
    @Column(name = "goods_subtitle")
    private String goodsSubtitle;

    /**
     * 计量单位
     */
    @Column(name = "goods_unit")
    @CanEmpty
    private String goodsUnit;

    /**
     * 商品主图
     */
    @Column(name = "goods_img")
    @CanEmpty
    private String goodsImg;

    /**
     * 商品重量
     */
    @Column(name = "goods_weight")
    private BigDecimal goodsWeight;

    /**
     * 市场价
     */
    @Column(name = "market_price")
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    @Column(name = "cost_price")
    @CanEmpty
    private BigDecimal costPrice;

    /**
     * 供货价
     */
    @Column(name = "supply_price")
    @CanEmpty
    private BigDecimal supplyPrice;

    /**
     * 建议零售价
     */
    @Column(name = "recommended_retail_price")
    @CanEmpty
    private BigDecimal recommendedRetailPrice;

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

    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 是否多规格标记
     */
    @Column(name = "more_spec_flag")
    private Integer moreSpecFlag;

    /**
     * 商品详情
     */
    @Column(name = "goods_detail")
    private String goodsDetail;

    /**
     * 商品移动端详情
     */
    @Column(name = "goods_mobile_detail")
    private String goodsMobileDetail;

    /**
     * 一对多关系，多个SKU编号
     */
    @Transient
    private List<String> goodsInfoIds;

    /**
     * 商品体积 单位：m3
     */
    @Column(name = "goods_cubage")
    private BigDecimal goodsCubage;

    /**
     * 商品视频链接
     */
    @Column(name = "goods_video")
    @CanEmpty
    private String goodsVideo;

    /**
     * 供应商名称
     */
    @Column(name = "provider_name")
    @CanEmpty
    private String providerName;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @Column(name = "goods_source")
    private Integer goodsSource;

    /**
     * 删除原因
     */
    @Column(name = "delete_reason")
    private String deleteReason;

    /**
     * 第三方平台的spuId
     */
    @Column(name = "third_platform_spu_id")
    private String thirdPlatformSpuId;
    /**
     * 第三方卖家id
     */
    @Column(name = "seller_id")
    private Long sellerId;

    /**
     * 三方渠道类目id
     */
    @Column(name = "third_cate_id")
    private Long thirdCateId;

    /**
     * 上下架状态,0:下架1:上架2:部分上架
     */
    @Column(name = "added_flag")
    private Integer addedFlag;


    /**
     * 店铺id
     */
    @Column(name = "store_id")
    private Long storeId;

    @Transient
    private long stock;

    @Transient
    private String providerGoodsId;

}
