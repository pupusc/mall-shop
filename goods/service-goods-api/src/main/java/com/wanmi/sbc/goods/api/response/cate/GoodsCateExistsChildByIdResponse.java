package com.wanmi.sbc.goods.api.response.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goodscate.GoodsCateExistsChildByIdResponse
 * 根据编号查询当前分类下面是否存在子分类响应对象
 * @author lipeng
 * @dateTime 2018/11/1 下午5:16
 */
@ApiModel
@Data
public class GoodsCateExistsChildByIdResponse implements Serializable {

    private static final long serialVersionUID = -1422780124511621182L;

    @ApiModelProperty(value = "是否存在子分类", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer result;
}
