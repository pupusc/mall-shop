package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsPropDetailRelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @discription 查询商品属性、图文信息
 * @author yangzhen
 * @date 2020/9/3 11:31
 * @param
 * @return
 */
@ApiModel
@Data
public class GoodsDetailProperResponse implements Serializable {

    private static final long serialVersionUID = -6641896293423917872L;

    /**
     * 商品图文信息
     */
    @ApiModelProperty(value = "商品图文信息")
    private String goodsDetail;

    /**
     * 商品属性列表
     */
    @ApiModelProperty(value = "商品属性列表")
    private List<GoodsPropDetailRelVO> goodsPropDetailRels;

}
