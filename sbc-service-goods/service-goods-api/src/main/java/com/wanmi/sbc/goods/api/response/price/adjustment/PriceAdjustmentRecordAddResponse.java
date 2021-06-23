package com.wanmi.sbc.goods.api.response.price.adjustment;

import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>调价记录表新增结果</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的调价记录表信息
     */
    @ApiModelProperty(value = "已新增的调价记录表信息")
    private PriceAdjustmentRecordVO priceAdjustmentRecordVO;
}
