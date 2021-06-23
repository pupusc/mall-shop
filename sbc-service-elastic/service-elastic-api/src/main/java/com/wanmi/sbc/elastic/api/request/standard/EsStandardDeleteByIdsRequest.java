package com.wanmi.sbc.elastic.api.request.standard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ES商品库删除请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsStandardDeleteByIdsRequest {

    /**
     * spuIds
     */
    @ApiModelProperty(value = "spuIds")
    private List<String> goodsIds;
}
