package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 商品实体类
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsPageSimpleVO implements Serializable {

    private static final long serialVersionUID = 2757888812286445293L;

    /**
     * 商品编号，采用UUID
     */
    @ApiModelProperty(value = "商品编号，采用UUID")
    private String goodsId;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;
    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    @ApiModelProperty(value = "商品类型")
    private Integer goodsType;
    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    @CanEmpty
    private Long brandId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * SPU编码
     */
    @ApiModelProperty(value = "SPU编码")
    private String goodsNo;

    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    @CanEmpty
    private String goodsImg;

    /**
     * 市场价
     */
    @ApiModelProperty(value = "市场价")
    @CanEmpty
    private BigDecimal marketPrice;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    @CanEmpty
    private BigDecimal supplyPrice;

    /**
     * 商品来源，0供应商，1商家
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家")
    private Integer goodsSource;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

    /**
     * 是否定时上架
     */
    @ApiModelProperty(value = "是否定时上架 true:是,false:否")
    private Boolean addedTimingFlag;

    /**
     * 定时上架时间
     */
    @ApiModelProperty(value = "定时上架时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTimingTime;

    /**
     * 是否多规格标记
     */
    @ApiModelProperty(value = "是否多规格标记", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer moreSpecFlag;

    /**
     * 设价类型 0:按客户 1:按订货量 2:按市场价
     */
    @ApiModelProperty(value = "设价类型", dataType = "com.wanmi.sbc.goods.bean.enums.PriceType")
    private Integer priceType;

    /**
     * 是否允许独立设价 0:不允许, 1:允许
     */
    @ApiModelProperty(value = "是否允许独立设价", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer allowPriceSet;

    /**
     * 是否可售
     */
    @ApiModelProperty(value = "是否可售", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer vendibility;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String supplierName;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 所属供应商商品Id
     */
    @ApiModelProperty(value = "所属供应商商品Id")
    private String providerGoodsId;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    @Enumerated
    private CheckStatus auditStatus;

    /**
     * 审核驳回原因
     */
    @ApiModelProperty(value = "审核驳回原因")
    private String auditReason;


    /**
     * 库存，根据相关所有SKU库存来合计
     */
    @ApiModelProperty(value = "库存，根据相关所有SKU库存来合计")
    private Long stock;

    /**
     * 一对多关系，多个SKU编号
     */
    @ApiModelProperty(value = "一对多关系，多个SKU编号")
    private List<String> goodsInfoIds;

    /**
     * 多对多关系，多个店铺分类编号
     */
    @ApiModelProperty(value = "多对多关系，多个店铺分类编号")
    private List<Long> storeCateIds;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型")
    private BoolFlag companyType;

    /**
     * 销售类别
     */
    @ApiModelProperty(value = "销售类别", dataType = "com.wanmi.sbc.goods.bean.enums.SaleType")
    private Integer saleType;

    /**
     * 商品销量
     */
    @ApiModelProperty(value = "商品销量")
    private Long goodsSalesNum;

    /**
     * 购买积分
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;

    /**
     * 注水销量
     */
    @ApiModelProperty(value = "注水销量")
    private Long shamSalesNum;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Long sortNo;

    /**
     * 是否单规格
     */
    @ApiModelProperty(value = "是否单规格")
    private Boolean singleSpecFlag;

    /**
     * 删除原因
     */
    @ApiModelProperty(value = "删除原因")
    private String deleteReason;

    /**
     * 下架原因
     */
    @Column(name = "下架原因")
    private String addFalseReason;


    /**
     * 购买方式 0立即购买,1购物车,内容以,相隔
     */
    @ApiModelProperty(value = "购买方式 0立即购买 1购物车,内容以,相隔")
    private String goodsBuyTypes;

    /**
     * 供应商店铺状态 0：关店 1：开店
     */
    @ApiModelProperty(value = "供应商店铺状态 0：关店 1：开店")
    private Integer providerStatus;

    /**
     * 是否禁止在新增拼团活动时选择
     */
    @ApiModelProperty(value = "是否禁止在新增拼团活动时选择")
    private boolean grouponForbiddenFlag;

    /**
     * 商品重量
     */
    @ApiModelProperty(value = "商品重量")
    private BigDecimal goodsWeight;

    /**
     * 商品体积 单位：m3
     */
    @ApiModelProperty(value = "商品体积", notes = "单位：m3")
    private BigDecimal goodsCubage;

    /**
     * 计量单位
     */
    @ApiModelProperty(value = "计量单位")
    private String goodsUnit;

    /**
     * 商品视频地址
     */
    @ApiModelProperty(value = "商品视频地址")
    private String goodsVideo;

    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情")
    private String goodsDetail;

    /**
     * 商品副标题
     */
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubtitle;
    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTempId;

    public Integer getVendibility(){
        if (Objects.nonNull(providerGoodsId)) {
            //供应商商品可售（商品上架、未删除、已审核，店铺开店）
            if((Objects.nonNull(vendibility) && DeleteFlag.YES.toValue() == vendibility)
                    && Constants.yes.equals(providerStatus)){
                return Constants.yes;
            } else {
                return Constants.no;
            }
        }
        return Constants.yes;
    }

    /**
     * 销售qudao
     */
    private List<String> goodsChannelTypeList;
}
