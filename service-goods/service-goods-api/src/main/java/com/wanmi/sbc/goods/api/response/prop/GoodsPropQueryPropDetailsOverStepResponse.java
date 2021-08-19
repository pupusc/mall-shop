package com.wanmi.sbc.goods.api.response.prop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 11:51
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPropQueryPropDetailsOverStepResponse implements Serializable {

    private static final long serialVersionUID = 1561282405432704982L;

    @ApiModelProperty(value = "保存默认spu与默认属性的关联")
    private Boolean result;
}
