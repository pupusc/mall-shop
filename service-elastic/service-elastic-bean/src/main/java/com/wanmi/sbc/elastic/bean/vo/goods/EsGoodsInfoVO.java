package com.wanmi.sbc.elastic.bean.vo.goods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ES商品实体类
 * 以SKU维度
 * Created by dyt on 2017/4/21.
 */
@Data
@ApiModel
public class EsGoodsInfoVO implements Serializable {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "goodsId")
    private String goodsId;

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
     * SKU信息
     */
    @ApiModelProperty(value = "SKU信息")
    private GoodsInfoNestVO goodsInfo;

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

    @ApiModelProperty(value = "属性id")
    private List<Long> propDetailIds;

    /**
     * 等级价数据
     */
    @ApiModelProperty(value = "等级价数据")
    private List<GoodsLevelPriceNestVO> goodsLevelPrices;

    /**
     * 客户价数据
     */
    @ApiModelProperty(value = "客户价数据")
    private List<GoodsCustomerPriceNestVO> customerPrices;

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
     * 可售状态 0不可售 1可售
     */
    @ApiModelProperty(value = "可售状态 0不可售 1可售")
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
    @ApiModelProperty(value = " 多对多关系，多个店铺分类编号")
    private List<Long> storeCateIds;

    /**
     * 分销商品状态，配合分销开关使用
     */
    @ApiModelProperty(value = "分销商品状态，配合分销开关使用")
    private Integer distributionGoodsStatus;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Long sortNo;

    @ApiModelProperty(value = "计算单位")
    private String goodsUnit;

    @ApiModelProperty(value = "划线价格")
    private BigDecimal linePrice;

    /**
     * 商品标签集合
     */
    @ApiModelProperty(value = "商品标签集合")
    private List<GoodsLabelNestVO> goodsLabelList;

    /**
     * 商品副标题
     */
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubtitle;

    @ApiModelProperty(value = "商品SPU NO")
    private String goodsNo;
}
