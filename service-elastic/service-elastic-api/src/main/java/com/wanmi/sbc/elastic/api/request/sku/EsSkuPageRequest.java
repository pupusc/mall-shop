package com.wanmi.sbc.elastic.api.request.sku;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsInfoSelectStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品Sku分页请求对象 - 应用于管理台
 *
 * @author dyt
 * @dateTime 2018/11/5 上午9:33
 */
@ApiModel
@Data
public class EsSkuPageRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = -7972557462976673056L;

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsInfoIds;
    /**
     * 排除批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> notGoodsInfoIds=new ArrayList<>();

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
     * 多个分类编号
     */
    @ApiModelProperty(value = "多个分类编号")
    private List<Long> cateIds;

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
     * 精确条件-批量SPU编码
     */
    @ApiModelProperty(value = "精确条件-批量SPU编码")
    private List<String> goodsNos;

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
     * 是否是加价购搜索
     */
    @ApiModelProperty(value = "true/false 是否是加价购搜索")
    private boolean isMarkup; 
    /**
     * 所在营销id
     */
    @ApiModelProperty(value = "所在营销id")
    private Long marketingId;
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
     * 商家类型
     */
    @ApiModelProperty(value = "商家类型", notes = "0、平台自营 1、第三方商家")
    private BoolFlag companyType;

    /**
     * 销售类别
     */
    @ApiModelProperty(value = "销售类别", notes = "0、批发 1、零售")
    private Integer saleType;

    /**
     * 商品来源，0供应商，1商家
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家")
    private Integer goodsSource;

    /**
     * 需要排除的三方渠道
     */
    private List<Integer> notThirdPlatformType;

    /**
     * 是否可售
     */
    private Integer vendibility;

    /**
     * 是否过滤积分价商品
     */
    private Boolean integralPriceFlag;

    /**
     * 标签ID
     */
    @ApiModelProperty(value = "标签ID")
    private Long labelId;

    /**
     * 是否显示购买积分
     */
    @ApiModelProperty(value = "是否显示购买积分 true:显示")
    private Boolean showPointFlag;

    @ApiModelProperty(value = "是否返回可售性 true:返回")
    private Boolean showVendibilityFlag;

    @ApiModelProperty(value = "是否返回供应商商品相关信息 true:返回")
    private Boolean showProviderInfoFlag;

    @ApiModelProperty(value = "是否填充LM商品库存")
    private Boolean fillLmInfoFlag;

    /**
     * ES过滤不必要的字段
     */
    private List<String> filterCols;


    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    @ApiModelProperty(value = "商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品")
    private Integer goodsType;


    /**
     * 周期购关联赠品，过滤周期购商品
     */
    @ApiModelProperty(value = "周期购关联赠品，过滤周期购商品")
    private Boolean cycleGift = Boolean.FALSE;
    /**
     * 过滤秒杀商品
     */
    @ApiModelProperty(value = "过滤秒杀商品")
    private Boolean flashSale = Boolean.FALSE;
    /**
     * 过滤预售商品
     */
    @ApiModelProperty(value = "过滤预售商品")
    private Boolean bookingSale = Boolean.FALSE;
    /**
     * 过滤加价购商品
     */
    @ApiModelProperty(value = "过滤加价购商品")
    private Boolean markup = Boolean.FALSE;
    /**
     * 过滤拼团商品
     */
    @ApiModelProperty(value = "过滤拼团商品")
    private Boolean groupon = Boolean.FALSE;

    /**
     * 赠品标记
     */
    private Integer giftFlag;
}
