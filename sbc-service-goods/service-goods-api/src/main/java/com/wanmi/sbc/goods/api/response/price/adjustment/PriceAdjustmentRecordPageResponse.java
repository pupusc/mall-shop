package com.wanmi.sbc.goods.api.response.price.adjustment;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>调价记录表分页结果</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 调价记录表分页结果
     */
    @ApiModelProperty(value = "调价记录表分页结果")
    private MicroServicePage<PriceAdjustmentRecordVO> priceAdjustmentRecordVOPage;
}
