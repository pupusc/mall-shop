package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsPropDetailRelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsPropDetailRelByIdsResponse
 * 根据多个SpuID查询属性关联响应对象
 * @author lipeng
 * @dateTime 2018/11/5 上午11:07
 */
@ApiModel
@Data
public class GoodsPropDetailRelByIdsResponse implements Serializable {

    private static final long serialVersionUID = -5556680709892895990L;

    @ApiModelProperty(value = "商品属性关联对象")
    private List<GoodsPropDetailRelVO> goodsPropDetailRelVOList;
}
