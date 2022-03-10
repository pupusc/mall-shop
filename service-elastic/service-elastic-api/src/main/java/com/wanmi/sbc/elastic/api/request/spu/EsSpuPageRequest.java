package com.wanmi.sbc.elastic.api.request.spu;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSelectStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSortType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsPageRequest
 * 商品分页请求对象 - 应用于管理台
 *
 * @author lipeng
 * @dateTime 2018/11/5 上午9:33
 */
@ApiModel
@Data
public class EsSpuPageRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = -7972557462976673056L;

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

    @ApiModelProperty(value = "精准条件-批量SKU编码")
    private List<String> goodsInfoNos;

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
     * 是否是团购搜索
     */
    @ApiModelProperty(value = "是否是团购搜索")
    private Boolean groupSearch;

    /**
     * 模糊条件-商品名称
     */
    @ApiModelProperty(value = "模糊条件-商品名称")
    private String likeGoodsName;

    /**
     * 模糊条件-供应商名称
     */
    @ApiModelProperty(value = "模糊条件-供应商名称")
    private String likeProviderName;

    /**
     * 模糊条件-关键词（商品名称、SPU编码）
     */
    @ApiModelProperty(value = "模糊条件-关键词", notes = "商品名称、SPU编码")
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
     * 批量非GoodsId
     */
    @ApiModelProperty(value = "非GoodsId")
    private List<String> notGoodsIdList;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String likeSupplierName;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态", dataType = "com.wanmi.sbc.goods.bean.enums.CheckStatus")
    private CheckStatus auditStatus;

    /**
     * 批量审核状态
     */
    @ApiModelProperty(value = "批量审核状态", dataType = "com.wanmi.sbc.goods.bean.enums.CheckStatus")
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
     * 销售类别
     */
    @ApiModelProperty(value = "销售类别", dataType = "com.wanmi.sbc.goods.bean.enums.SaleType")
    private Integer saleType;

    /**
     * 商品类型，0：实体商品，1：虚拟商品，2：卡券商品，3：周期购商品
     */
    @ApiModelProperty(value = "商品类型")
    private Integer goodsType;

    /**
     * 商品来源，0品牌商城，1商家
     */
    @ApiModelProperty(value = "商品来源，0品牌商城，1商家")
    private Integer goodsSource;

    /**
     * 是否显示购买积分
     */
    @ApiModelProperty(value = "是否显示购买积分 true:显示")
    private Boolean showPointFlag;

    /**
     * 排序类型
     */
    @ApiModelProperty(value = "排序类型")
    private GoodsSortType goodsSortType;

    /**
     * 是否显示可售状态购买积分
     */
    @ApiModelProperty(value = "是否显示可售状态 true:显示")
    private Boolean showVendibilityFlag;

    /**
     * 标签ID
     */
    @ApiModelProperty(value = "标签ID")
    private Long labelId;


    /**
     * ERP的SPU编码
     */
    @ApiModelProperty(value = "ERP的SPU编码")
    private String spuErp;

    /**
     * 商品销售平台
     */
    private List<String> goodsChannelType;
}
