package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest
 * 根据编号查询商品信息请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午9:40
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsByIdRequest implements Serializable {

    private static final long serialVersionUID = 5594325220431537194L;

    @ApiModelProperty(value = "商品Id")
    @NotBlank
    private String goodsId;
}
