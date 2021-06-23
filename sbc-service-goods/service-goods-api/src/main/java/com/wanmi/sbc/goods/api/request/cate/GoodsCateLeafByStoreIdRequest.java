package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>根据店铺获取叶子分类列表请求结构</p>
 * @author daiyitian
 * @dateTime 2018/12/19 下午4:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateLeafByStoreIdRequest implements Serializable {

    private static final long serialVersionUID = 9076707290877278591L;

    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
