package com.wanmi.sbc.goods.api.request.enterprise.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsInfoSelectStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品SKU分页查询请求
 * Created by 柏建忠 on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseGoodsInfoPageRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = -8312305040718761890L;

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsInfoIds;

    /**
     * SPU编号
     */
    @ApiModelProperty(value = "SPU编号")
    private String goodsId;

    /**
     * 批量SPU编号
     */
    @ApiModelProperty(value = "批量SPU编号")
    private List<String> goodsIds;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * 批量品牌编号
     */
    @ApiModelProperty(value = "批量品牌编号")
    private List<Long> brandIds;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 店铺分类id
     */
    @ApiModelProperty(value = "店铺分类id")
    private Long storeCateId;

    /**
     * 模糊条件-商品名称
     */
    @ApiModelProperty(value = "模糊条件-商品名称")
    private String likeGoodsName;

    /**
     * 精确条件-批量SKU编码
     */
    @ApiModelProperty(value = "精确条件-批量SKU编码")
    private List<String> goodsInfoNos;

    /**
     * 模糊条件-SKU编码
     */
    @ApiModelProperty(value = "模糊条件-SKU编码")
    private String likeGoodsInfoNo;

    /**
     * 模糊条件-SPU编码
     */
    @ApiModelProperty(value = "模糊条件-SPU编码")
    private String likeGoodsNo;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

    /**
     * 上下架状态-批量
     */
    @ApiModelProperty(value = "上下架状态-批量", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private List<Integer> addedFlags;

    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer delFlag;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 客户等级
     */
    @ApiModelProperty(value = "客户等级")
    private Long customerLevelId;

    /**
     * 客户等级折扣
     */
    @ApiModelProperty(value = "客户等级折扣")
    private BigDecimal customerLevelDiscount;

    /**
     * 非GoodsId
     */
    @ApiModelProperty(value = "非GoodsId")
    private String notGoodsId;

    /**
     * 非GoodsInfoId
     */
    @ApiModelProperty(value = "非GoodsInfoId")
    private String notGoodsInfoId;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 批量店铺ID
     */
    @ApiModelProperty(value = "批量店铺ID")
    private List<Long> storeIds;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态", notes = "0：待审核 1：已审核 2：审核失败 3：禁售中")
    private CheckStatus auditStatus;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态", notes = "0：待审核 1：已审核 2：审核失败 3：禁售中")
    private List<CheckStatus> auditStatuses;

    /**
     * 关键词，目前范围：商品名称、SKU编码
     */
    @ApiModelProperty(value = "关键词，目前范围：商品名称、SKU编码")
    private String keyword;

    /**
     * 业务员app,商品状态筛选
     */
    @ApiModelProperty(value = "业务员app,商品状态筛选", notes = "0：上架中 1：下架中 2：待审核及其他")
    private List<GoodsInfoSelectStatus> goodsSelectStatuses;

    /**
     * 企业购商品审核状态
     */
    @ApiModelProperty(value = "企业购商品审核状态", notes = "0：无状态 1：待审核 2：已审核 3：审核未通过")
    private EnterpriseAuditState enterPriseAuditState;

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
     * 库存范围参数1
     */
    @ApiModelProperty(value = "库存范围参数1")
    private Long stockFirst;

    /**
     * 库存范围参数2
     */
    @ApiModelProperty(value = "库存范围参数2")
    private Long stockLast;

    /**
     * 市场价范围参数1
     */
    private BigDecimal salePriceFirst;

    /**
     * 市场价范围参数2
     */
    private BigDecimal salePriceLast;

    /**
     * 标签ID
     */
    @ApiModelProperty(value = "标签ID")
    private Long labelId;

}
