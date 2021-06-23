package com.wanmi.sbc.goods.api.request.standard;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>商品库查询分页请求类</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardGoodsPageRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = -3693107556414202757L;

    /**
     * 批量SPU编号
     */
    @ApiModelProperty(value = "spu Id")
    private List<String> goodsIds;

    /**
     * 模糊条件-商品名称
     */
    @ApiModelProperty(value = "模糊条件-商品名称")
    private String likeGoodsName;

    /**
     * 模糊条件-商品名称
     */
    @ApiModelProperty(value = "模糊条件-供应商名称")
    private String likeProviderName;

    @ApiModelProperty(value = "模糊条件-sku")
    private String likeGoodsInfoNo;

    @ApiModelProperty(value = "模糊条件-spu")
    private String likeGoodsNo;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家，2 linkedmall")
    private Integer goodsSource;

    /**
     * 商品来源，0供应商，1商家
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家,2收钱吧,3boss")
    private List<Integer> goodsSourceList;

    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类")
    private Long cateId;

    /**
     * 批量商品分类
     */
    @ApiModelProperty(value = "批量商品分类")
    private List<Long> cateIds;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * 批量品牌分类
     */
    @ApiModelProperty(value = "批量品牌分类")
    private List<Long> brandIds;

    /**
     * 批量品牌分类，可与NULL以or方式查询
     */
    @ApiModelProperty(value = "批量品牌分类，可与NULL以or方式查询")
    private List<Long> orNullBrandIds;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;

    /**
     * 非GoodsId
     */
    @ApiModelProperty(value = "非GoodsId")
    private String notGoodsId;
    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;
    /**
     * 导入状态  -1 全部 1 已导入 2未导入
     */
    @ApiModelProperty(value = "导入状态 -1 全部   1已导入 2未导入")
    private Integer toLeadType;

    @ApiModelProperty("上下架状态,0:下架1:上架2:部分上架")
    private Integer addedFlag;

    @ApiModelProperty("三方渠道类型，0 linkedmall")
    private ThirdPlatformType thirdPlatformType;

    /**
     * 创建开始时间
     */
    @ApiModelProperty(value = "创建开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;

    /**
     * 创建结束时间
     */
    @ApiModelProperty(value = "创建结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;
}
