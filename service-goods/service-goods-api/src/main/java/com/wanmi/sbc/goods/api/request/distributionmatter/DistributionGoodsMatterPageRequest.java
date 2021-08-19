package com.wanmi.sbc.goods.api.request.distributionmatter;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.MatterType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.util.List;

@Data
@ApiModel
public class DistributionGoodsMatterPageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 3526136339091603088L;

    @NotBlank
    @ApiModelProperty(value = "商品skuid")
    private String goodsInfoId;

    @ApiModelProperty(value = "发布者id")
    private String operatorId;

    @ApiModelProperty(value = "引用次数范围（小）")
    private Integer recommendNumMin;

    @ApiModelProperty(value = "引用次数范围（大）")
    private Integer recommendNumMax;

    @ApiModelProperty(value = "引用次数排序")
    private SortType sortByRecommendNum;


    @ApiModelProperty(value = "商品名称")
    private String goodsInfoName;

    @ApiModelProperty(value = "Sku编码")
    private String goodsInfoNo;

    /**
     * 平台类目-仅限三级类目
     */
    @ApiModelProperty(value = "平台类目")
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
     * 素材类型
     */
    @ApiModelProperty(value = "素材类型")
    private MatterType matterType;

    /**
     * 登录人id,用来查询分销员等级
     */
    @ApiModelProperty(value="customerId")
    private String customerId;

    /**
     * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
     */
    @ApiModelProperty(value = "分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销")
    private DistributionGoodsAudit distributionGoodsAudit;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;
}
