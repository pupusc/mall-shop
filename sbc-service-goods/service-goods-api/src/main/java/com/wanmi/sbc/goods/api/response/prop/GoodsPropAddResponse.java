package com.wanmi.sbc.goods.api.response.prop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/10/31 14:53
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPropAddResponse implements Serializable{

    private static final long serialVersionUID = 2901653861476077348L;

    @ApiModelProperty(value = "spu Id")
    private List<String> stringList;
}
