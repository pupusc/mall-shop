package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品SPU属性查询请求
 * Created by bail on 2018/3/23.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class EsPropQueryRequest extends BaseQueryRequest {

    // 搜索属性
    @ApiModelProperty(value = "搜索属性")
    private Long propId;

    // 搜索属性值
    @ApiModelProperty(value = "搜索属性值")
    private List<Long> detailIds = new ArrayList<>();
}
