package com.wanmi.sbc.elastic.bean.vo.goods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * ES商品实体类
 * 以SKU维度
 * Created by dyt on 2017/4/21.
 */
@Data
@ApiModel
public class EsGoodsVO implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 转化为小写
     */
    @ApiModelProperty(value = "转化为小写")
    private String lowGoodsName;

    /**
     * 转化为小写
     */
    @ApiModelProperty(value = "转化为小写")
    private String pinyinGoodsName;

    /**
     * SKU信息auditStatus
     */
    @ApiModelProperty(value = "SKU信息auditStatus")
    private List<GoodsInfoNestVO> goodsInfos;

    /**
     * 商品分类信息
     */
    @ApiModelProperty(value = "商品分类信息")
    private GoodsCateNestVO goodsCate;

    /**
     * 商品品牌信息
     */
    @ApiModelProperty(value = "商品品牌信息")
    private GoodsBrandNestVO goodsBrand;

    /**
     * 上下架时间
     */
    @ApiModelProperty(value = "上下架时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime addedTime;

    /**
     * SKU相关规格
     */
    @ApiModelProperty(value = "SKU相关规格")
    private List<GoodsInfoSpecDetailRelNestVO> specDetails;

    @ApiModelProperty(value = "属性id")
    private List<Long> propDetailIds;

    /**
     * 等级价数据
     */
    @ApiModelProperty(value = "等级价数据")
    private List<GoodsLevelPriceNestVO> goodsLevelPrices = new ArrayList<>();

    /**
     * 客户价数据
     */
    @ApiModelProperty(value = "客户价数据")
    private List<GoodsCustomerPriceNestVO> customerPrices = new ArrayList<>();

    /**
     * 签约开始日期
     */
    @ApiModelProperty(value = "签约开始日期")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractStartDate;

    /**
     * 签约结束日期
     */
    @ApiModelProperty(value = "签约结束日期")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @ApiModelProperty(value = "店铺状态 0、开启 1、关店")
    private Integer storeState;

    /**
     * 可售状态 0不可收 1可售
     */
    @ApiModelProperty(value = "可售状态 0不可收 1可售")
    private Integer vendibilityStatus;

    /**
     * 禁售状态
     */
    @ApiModelProperty(value = "禁售状态")
    private Integer forbidStatus;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态")
    private Integer auditStatus;

    /**
     * 多对多关系，多个店铺分类编号
     */
    @ApiModelProperty(value = "多对多关系，多个店铺分类编号")
    private List<Long> storeCateIds;

    /**
     * 营销信息
     */
    @ApiModelProperty(value = "营销信息")
    private List<MarketingForEndVO> marketingList = new ArrayList<>();

    /**
     * 分销商品状态，配合分销开关使用
     */
    @ApiModelProperty(value = "分销商品状态，配合分销开关使用")
    private Integer distributionGoodsStatus;

    /**
     * 商品评论数
     */
    @ApiModelProperty(value = "商品评论数")
    private Long goodsEvaluateNum;

    /**
     * 商品收藏量
     */
    @ApiModelProperty(value = "商品收藏量")
    private Long goodsCollectNum;

    /**
     * 商品销量
     */
    @ApiModelProperty(value = "商品销量")
    private Long goodsSalesNum;

    /**
     * 真实的商品销量
     */
    @ApiModelProperty(value = "真实商品销量")
    private Long realGoodsSalesNum;

    /**
     * 商品好评数量
     */
    @ApiModelProperty(value = "商品好评数量")
    private Long goodsFavorableCommentNum;

    /**
     * 商品好评率
     */
    @ApiModelProperty(value = "商品好评率")
    private Long goodsFeedbackRate;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Long sortNo;

    /**
     * 购买积分
     */
    @ApiModelProperty(value = "购买积分")
    private Long buyPoint;

    /**
     * 排序的价格
     */
    @ApiModelProperty(value = "排序的价格")
    private BigDecimal esSortPrice;

    @ApiModelProperty(value = "计算单位")
    private String goodsUnit;

    @ApiModelProperty(value = "划线价格")
    private BigDecimal linePrice;

    /**
     * 三方渠道类型，0 linkedmall
     */
    @ApiModelProperty(value = "三方渠道类型，0 linkedmall")
    private ThirdPlatformType thirdPlatformType;

    /**
     * 商品标签集合
     */
    @ApiModelProperty(value = "商品标签集合")
    private List<GoodsLabelNestVO> goodsLabelList;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String supplierName;


    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码")
    private String goodsNo;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态")
    private Integer addedFlag;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家,2 linkedmall ")
    private Integer goodsSource;

    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    @ApiModelProperty(value = "商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品")
    private Integer goodsType;
    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    @ApiModelProperty(value = "知识顾问专享 0:不是 ，1：是")
    private Integer cpsSpecial;

    /**
     * 主播推荐 1樊登解读,2非凡精读,3樊登直播 内容以 相隔
     */
    @ApiModelProperty(value = "主播推荐 1樊登解读,2非凡精读,3樊登直播 内容以 相隔")
    private String anchorPushs;
    /**
     * 商品库存
     */
    @ApiModelProperty(value = "商品库存")
    private Long stock;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "商品标题")
    private String goodsName;

    /**
     * 商品副标题
     */
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubtitle;

    /**
     * 无背景图片
     */
    @ApiModelProperty(value = "无背景图片")
    private String goodsUnBackImg;

    /**
     * 扩展属性信息
     */
    private EsExtPropsVo goodsExtProps;

    /**
     * 销售渠道
     */
    private Set<Integer> goodsChannelTypeSet;
}
