package com.wanmi.sbc.goods.api.response.price.adjustment;

import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>调价记录表列表结果</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 调价记录表列表结果
     */
    @ApiModelProperty(value = "调价记录表列表结果")
    private List<PriceAdjustmentRecordVO> priceAdjustmentRecordVOList;
}
