package com.wanmi.sbc.goods.standard.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品库SKU实体类
 * Created by dyt on 2017/4/11.
 */
@Data
@Entity
@Table(name = "standard_sku")
public class StandardSku implements Serializable {

    /**
     * 商品SKU编号
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "goods_info_id")
    private String goodsInfoId;

    /**
     * 商品编号
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 商品SKU编码
     */
    @Column(name = "goods_info_no")
    private String goodsInfoNo;

    /**
     * 商品SKU名称
     */
    @Column(name = "goods_info_name")
    private String goodsInfoName;

    /**
     * 商品图片
     */
    @Column(name = "goods_info_img")
    @CanEmpty
    private String goodsInfoImg;

    /**
     * 商品市场价
     */
    @Column(name = "market_price")
    private BigDecimal marketPrice;

    /**
     * 商品成本价
     */
    @CanEmpty
    @Column(name = "cost_price")
    private BigDecimal costPrice;

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
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @Column(name = "goods_source")
    private Integer goodsSource;

    /**
     * 新增时，模拟多个规格ID
     * 查询详情返回响应，扁平化多个规格ID
     */
    @Transient
    private List<Long> mockSpecIds;

    /**
     * 新增时，模拟多个规格值 ID
     * 查询详情返回响应，扁平化多个规格值ID
     */
    @Transient
    private  List<Long> mockSpecDetailIds;

    /**
     * 商品分页，扁平化多个商品规格值ID
     */
    @Transient
    private List<Long> specDetailRelIds;

    /**
     * 规格名称规格值 颜色:红色;大小:16G
     */
    @Transient
    private String specText;

    /**
     * 供应商商品sku
     */
    @Column(name = "provider_goods_info_id")
    private String providerGoodsInfoId;

    /**
     * 商品供货价
     */
    @Column(name = "supply_price")
    private BigDecimal supplyPrice;

    /**
     * 商品库存
     */
    @Column(name = "stock")
    private Long stock;

    /**
     * 第三方平台的skuId
     */
    @Column(name = "third_platform_sku_id")
    private String thirdPlatformSkuId;

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


}
