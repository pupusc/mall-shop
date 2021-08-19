package com.wanmi.sbc.goods.api.response.storecate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/5 11:42
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateQueryHasChildResponse implements Serializable {
    private static final long serialVersionUID = 9198381274384416414L;

    @ApiModelProperty(value = "是否有子类", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer result;
}
