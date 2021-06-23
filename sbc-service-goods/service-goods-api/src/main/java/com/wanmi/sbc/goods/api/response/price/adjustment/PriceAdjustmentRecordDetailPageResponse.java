package com.wanmi.sbc.goods.api.response.price.adjustment;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>调价单详情表分页结果</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordDetailPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 调价单详情表分页结果
     */
    @ApiModelProperty(value = "调价单详情表分页结果")
    private MicroServicePage<PriceAdjustmentRecordDetailVO> recordDetailPage;
}
