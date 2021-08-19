package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 根据商品SKU编号删除请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoDeleteByIdsRequest implements Serializable {

    private static final long serialVersionUID = -2265501195719873212L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    @NotEmpty
    private List<String> goodsInfoIds;
}
