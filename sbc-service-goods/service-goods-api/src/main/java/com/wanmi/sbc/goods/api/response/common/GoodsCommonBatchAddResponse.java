package com.wanmi.sbc.goods.api.response.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 批量导入商品信息响应对象
 * @author daiyitian
 * @dateTime 2018/11/2 上午9:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCommonBatchAddResponse implements Serializable {

    private static final long serialVersionUID = 2373497523561481281L;

    /**
     * 批量新增商品的id
     */
    @ApiModelProperty(value = "批量新增商品的id")
    private List<String> skuNoList;
}
