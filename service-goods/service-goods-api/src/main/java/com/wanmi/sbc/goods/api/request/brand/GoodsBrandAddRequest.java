package com.wanmi.sbc.goods.api.request.brand;

import com.wanmi.sbc.goods.bean.dto.GoodsBrandDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 品牌新增请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsBrandAddRequest extends GoodsBrandDTO {

    private static final long serialVersionUID = 8253508996955495630L;
}
