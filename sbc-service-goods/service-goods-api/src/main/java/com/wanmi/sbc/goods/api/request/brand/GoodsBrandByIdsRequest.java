package com.wanmi.sbc.goods.api.request.brand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌删除请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBrandByIdsRequest implements Serializable {

    private static final long serialVersionUID = 4390819159191294564L;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    @NotEmpty
    private List<Long> brandIds;
}
