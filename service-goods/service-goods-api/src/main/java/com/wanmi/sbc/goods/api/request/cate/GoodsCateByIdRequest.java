package com.wanmi.sbc.goods.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goodscate.GoodsCateByIdRequest
 * 根据分类编号查询商品信息请求对象
 * @author lipeng
 * @dateTime 2018/11/1 下午4:41
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateByIdRequest implements Serializable {

    private static final long serialVersionUID = 9076707290877278591L;

    @ApiModelProperty(value = "分类Id")
    private Long cateId;
}
