package com.wanmi.sbc.goods.api.response.freight;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据运费模板id和店铺id获取区域id响应
 * Created by daiyitian on 2018/5/3.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateStoreAreaIdByIdAndStoreIdResponse implements Serializable {

    private static final long serialVersionUID = 8246352610810060194L;

    /**
     * 区域ids
     */
    @ApiModelProperty(value = "区域ids")
    private List<Long> areaIds;


}
