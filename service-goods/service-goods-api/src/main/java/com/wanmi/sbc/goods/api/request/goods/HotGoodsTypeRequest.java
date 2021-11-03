package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;



@EqualsAndHashCode(callSuper = true)
@Data
public class HotGoodsTypeRequest extends BaseRequest {

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    private List<Integer> types;


}
