package com.wanmi.sbc.goods.api.response.storecate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 11:43
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateQueryHasGoodsResponse implements Serializable {
    private static final long serialVersionUID = 7183385983389154743L;

    @ApiModelProperty(value = "是否有子类", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer result;
}
