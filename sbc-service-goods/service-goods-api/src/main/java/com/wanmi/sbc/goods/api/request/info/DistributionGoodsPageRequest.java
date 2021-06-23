package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsPageRequest
 * 分销商品分页请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@ApiModel
@Data
public class DistributionGoodsPageRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = 1976608100715393368L;

    /**
     * 模糊条件-商品名称
     */
    @ApiModelProperty(value = "模糊条件-商品名称")
    private String likeGoodsName;

    /**
     * 模糊条件-SKU编码
     */
    @ApiModelProperty(value = "模糊条件-SKU编码")
    private String likeGoodsInfoNo;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 店铺分类Id
     */
    @ApiModelProperty(value = "店铺分类Id")
    private Long storeCateId;

    /**
     * 平台类目-仅限三级类目
     */
    @ApiModelProperty(value = "平台类目-仅限三级类目")
    private Long cateId;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

    /**
     * 市场价范围参数1
     */
    @ApiModelProperty(value = "第一个市场价")
    private BigDecimal salePriceFirst;

    /**
     * 市场价范围参数2
     */
    @ApiModelProperty(value = "第二个市场价")
    private BigDecimal salePriceLast;

    /**
     * 分销佣金范围参数1
     */
    @ApiModelProperty(value = "第一个分销佣金")
    private BigDecimal distributionCommissionFirst;

    /**
     * 分销佣金范围参数2
     */
    @ApiModelProperty(value = "第二个分销佣金")
    private BigDecimal distributionCommissionLast;

    /**
     * 佣金比例范围参数1
     */
    @ApiModelProperty(value = "第一个佣金比例")
    private BigDecimal commissionRateFirst;

    /**
     * 佣金比例范围参数2
     */
    @ApiModelProperty(value = "第二个佣金比例")
    private BigDecimal commissionRateLast;

    /**
     * 分销销量范围参数1
     */
    @ApiModelProperty(value = "第一个分销销量")
    private Integer distributionSalesCountFirst;

    /**
     * 分销销量范围参数2
     */
    @ApiModelProperty(value = "第二个分销销量")
    private Integer distributionSalesCountLast;

    /**
     * 库存范围参数1
     */
    @ApiModelProperty(value = "第一个库存范围")
    private Long stockFirst;

    /**
     * 库存范围参数2
     */
    @ApiModelProperty(value = "第二个库存范围")
    private Long stockLast;

    /**
     * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
     */
    @NotNull
    @ApiModelProperty(value = "分销商品审核状态", dataType = "com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit")
    private Integer distributionGoodsAudit;

    /**
     * 是否过滤商品状态为失效的商品  0 否 1 是
     * 商品状态 0：正常 1：缺货 2：失效
     */
    @ApiModelProperty(value = "是否过滤商品状态为失效的商品", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Integer goodsStatus ;

    /**
     * 销售类型 0:批发, 1:零售
     */
    @ApiModelProperty(value = "销售类型", dataType = "com.wanmi.sbc.goods.bean.enums.SaleType")
    private Integer saleType;

    /**
     * 标签ID
     */
    @ApiModelProperty(value = "标签ID")
    private Long labelId;
}
