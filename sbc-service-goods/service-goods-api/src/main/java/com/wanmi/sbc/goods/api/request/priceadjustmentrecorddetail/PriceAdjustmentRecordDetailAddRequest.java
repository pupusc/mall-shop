package com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail;

import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>调价单详情表新增参数</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PriceAdjustmentRecordDetailAddRequest extends PriceAdjustmentRecordDetailDTO {

	private static final long serialVersionUID = -2429623288175137547L;
}