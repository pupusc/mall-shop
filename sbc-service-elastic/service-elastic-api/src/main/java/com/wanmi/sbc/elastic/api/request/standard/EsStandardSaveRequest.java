package com.wanmi.sbc.elastic.api.request.standard;

import com.wanmi.sbc.elastic.bean.dto.goods.EsStandardGoodsDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ES商品库保存请求
 * Created by daiyitian on 2017/3/24.
 */

@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class EsStandardSaveRequest extends EsStandardGoodsDTO {
}
