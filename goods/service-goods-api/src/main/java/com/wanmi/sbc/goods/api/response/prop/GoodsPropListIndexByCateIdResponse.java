package com.wanmi.sbc.goods.api.response.prop;

import com.wanmi.sbc.goods.bean.vo.GoodsPropVO;
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
public class GoodsPropListIndexByCateIdResponse implements Serializable{

    private static final long serialVersionUID = -1414809542583853241L;

    @ApiModelProperty(value = "商品属性")
    private List<GoodsPropVO> goodsPropVOList;
}
