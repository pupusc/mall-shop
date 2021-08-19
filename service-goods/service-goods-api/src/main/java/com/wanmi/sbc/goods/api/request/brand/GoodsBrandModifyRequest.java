package com.wanmi.sbc.goods.api.request.brand;

import com.wanmi.sbc.goods.bean.dto.GoodsBrandDTO;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 品牌新增请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
public class GoodsBrandModifyRequest extends GoodsBrandDTO {

    private static final long serialVersionUID = 6755281517285771010L;
}
