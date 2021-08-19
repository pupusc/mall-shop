package com.wanmi.sbc.goods.api.request.spec;

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
 * 根据goodsIds批量查询请求结构
 * @author daiyitian
 * @dateTime 2018/11/13 上午9:46
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSpecListByGoodsIdsRequest implements Serializable {

    private static final long serialVersionUID = -805979507477447007L;

    @ApiModelProperty(value = "spu Id")
    @NotEmpty
    private List<String> goodsIds;
}
