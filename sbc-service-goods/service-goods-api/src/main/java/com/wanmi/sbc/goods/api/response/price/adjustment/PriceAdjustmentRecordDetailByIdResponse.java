package com.wanmi.sbc.goods.api.response.price.adjustment;

import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）调价单详情表信息response</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordDetailByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 调价单详情表信息
     */
    @ApiModelProperty(value = "调价单详情表信息")
    private PriceAdjustmentRecordDetailVO priceAdjustmentRecordDetailVO;
}
