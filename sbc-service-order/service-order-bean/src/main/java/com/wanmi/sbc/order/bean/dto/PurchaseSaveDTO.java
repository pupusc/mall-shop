package com.wanmi.sbc.order.bean.dto;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.customer.bean.dto.CustomerLevelDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.order.bean.enums.FollowFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-30
 */
@Data
@Builder
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseSaveDTO implements Serializable {

    private static final long serialVersionUID = -2197802654540670472L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private List<Long> followIds;

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
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    @Range(min = 1)
    private Long goodsNum;

    /**
     * 收藏标识
     */
    @Enumerated
    @ApiModelProperty(value = "收藏标识")
    private FollowFlag followFlag;

    /**
     * 校验库存
     */
    @ApiModelProperty(value = "是否校验库存",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    @Builder.Default
    private Boolean verifyStock = true;

    /**
     * 当前客户等级
     */
    @ApiModelProperty(value = "当前客户等级")
    private CustomerLevelDTO customerLevel;

    /**
     * 是否赠品 true 是 false 否
     */
    @ApiModelProperty(value = "是否赠品",dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    @Builder.Default
    private Boolean isGift = false;

    /**
     * 邀请人id-会员id
     */
    @ApiModelProperty(value = "邀请人id")
    String inviteeId;

    /**
     * 终端来源
     */
    @ApiModelProperty(value = "终端来源", hidden = true)
    private TerminalSource terminalSource;
    
}
