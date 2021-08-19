package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.order.bean.enums.FollowFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Enumerated;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-12-03
 */
@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseQueryDTO extends BaseQueryRequest {

    private static final long serialVersionUID = 3635486109211612000L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsInfoIds;

    /**
     * 批量sku
     */
    @ApiModelProperty(value = "批量sku")
    private List<GoodsInfoDTO> goodsInfos;

    /**
     * 会员编号
     */
    @ApiModelProperty(value = "会员编号")
    private String customerId;

    /**
     * 收藏标识
     */
    @Enumerated
    @ApiModelProperty(value = "收藏标识")
    private FollowFlag followFlag;

    /**
     * 校验库存
     */
    @ApiModelProperty(value = "是否校验库存", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    @Default
    private Boolean verifyStock = true;

    /**
     * 当前客户等级
     */
    @ApiModelProperty(value = "当前客户等级")
    private CustomerLevelVO customerLevel;

    /**
     * 是否赠品 true 是 false 否
     */
    @ApiModelProperty(value = "是否赠品", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    @Default
    private Boolean isGift = false;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;

    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    @ApiModelProperty("区的区域码")
    private Long areaId;
}
