package com.wanmi.sbc.goods.api.request.enterprise.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.dto.BatchEnterPrisePriceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 批量新增企业商品请求
 * @author baijianzhong
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterprisePriceBatchUpdateRequest extends BaseQueryRequest implements Serializable {

    private static final long serialVersionUID = -8312305040718761890L;

    /**
     * 批量修改企业价格
     */
    @ApiModelProperty(value = " 批量修改企业价格 ")
    List<BatchEnterPrisePriceDTO> batchEnterPrisePriceDTOS;

    /**
     *  企业商品审核 0: 不需要审核 1: 需要审核
     */
    @ApiModelProperty(value = " 企业商品审核 0: 不需要审核 1: 需要审核 ")
    private DefaultFlag enterpriseGoodsAuditFlag;



}
