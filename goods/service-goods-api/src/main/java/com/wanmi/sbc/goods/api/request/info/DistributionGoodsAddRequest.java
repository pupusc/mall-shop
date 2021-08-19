package com.wanmi.sbc.goods.api.request.info;

import com.wanmi.sbc.goods.bean.dto.DistributionGoodsInfoModifyDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.info.DistributionGoodsAddRequest
 * 添加分销商品请求对象
 *
 * @author CHENLI
 * @dateTime 2019/2/19 上午9:33
 */
@ApiModel
@Data
public class DistributionGoodsAddRequest implements Serializable {

    private static final long serialVersionUID = 8856669437653749450L;

    /**
     * 批量添加分销商品
     */
    @ApiModelProperty(value = "批量skuIds")
    @NotNull
    private List<DistributionGoodsInfoModifyDTO> distributionGoodsInfoModifyDTOS;

    /**
     * 分销商品审核状态 0:普通商品 1:待审核 2:已审核通过 3:审核不通过 4:禁止分销
     */
    @ApiModelProperty(value = "分销商品审核状态")
    @NotNull
    private DistributionGoodsAudit distributionGoodsAudit;
}
