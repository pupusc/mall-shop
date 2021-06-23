package com.wanmi.sbc.goods.api.response.price.adjustment;

import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>调价单详情表列表结果</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordDetailListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 调价单详情表列表结果
     */
    @ApiModelProperty(value = "调价单详情表列表结果")
    private List<PriceAdjustmentRecordDetailVO> priceAdjustmentRecordDetailVOList;
}
