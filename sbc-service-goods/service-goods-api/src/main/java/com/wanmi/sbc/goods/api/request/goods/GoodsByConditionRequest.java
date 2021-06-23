package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSelectStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 商品查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsByConditionRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 7979034129957350808L;

    /**
     * 批量SPU编号
     */
    @ApiModelProperty(value = "批量SPU编号")
    private List<String> goodsIds;

    /**
     * 精准条件-SPU编码
     */
    @ApiModelProperty(value = "精准条件-SPU编码")
    private String goodsNo;

    /**
     * 精准条件-批量SPU编码
     */
    @ApiModelProperty(value = "精准条件-批量SPU编码")
    private List<String> goodsNos;

    /**
     * 模糊条件-SPU编码
     */
    @ApiModelProperty(value = "模糊条件-SPU编码")
    private String likeGoodsNo;

    /**
     * 模糊条件-SKU编码
     */
    @ApiModelProperty(value = "模糊条件-SKU编码")
    private String likeGoodsInfoNo;

    /**
     * 模糊条件-商品名称
     */
    @ApiModelProperty(value = "模糊条件-商品名称")
    private String likeGoodsName;

    /**
     * 模糊条件-关键词（商品名称、SPU编码）
     */
    @ApiModelProperty(value = "模糊条件-关键词（商品名称、SPU编码）")
    private String keyword;

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
     * 批量品牌编号
     */
    @ApiModelProperty(value = "批量品牌编号")
    private List<Long> brandIds;

    /**
     * 上下架状态
     */
    @ApiModelProperty(value = "上下架状态", dataType = "com.wanmi.sbc.goods.bean.enums.AddedFlag")
    private Integer addedFlag;

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
     * 非GoodsId
     */
    @ApiModelProperty(value = "非GoodsId")
    private String notGoodsId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String likeSupplierName;

    /**
     * 供应商商品Ids
     */
    @ApiModelProperty(value = "批量供应商商品Ids")
    private List<String> providerGoodsIds;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态", notes = "0：待审核 1：已审核 2：审核失败 3：禁售中")
    private CheckStatus auditStatus;

    /**
     * 批量审核状态
     */
    @ApiModelProperty(value = "批量审核状态")
    private List<CheckStatus> auditStatusList;

    /**
     * 店铺分类Id
     */
    @ApiModelProperty(value = "店铺分类Id")
    private Long storeCateId;

    /**
     * 店铺分类所关联的SpuIds
     */
    @ApiModelProperty(value = "店铺分类所关联的SpuIds")
    private List<String> storeCateGoodsIds;

    /**
     * 运费模板ID
     */
    @ApiModelProperty(value = "运费模板ID")
    private Long freightTempId;

    /**
     * 商品状态筛选
     */
    @ApiModelProperty(value = "商品状态筛选")
    private List<GoodsSelectStatus> goodsSelectStatuses;

    /**
     * 商品来源，0供应商，1商家
     */
    private Integer goodsSource;

}
