package com.wanmi.sbc.goods.api.response.goods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsPageResponse
 * 商品分页响应对象
 *
 * @author lipeng
 * @dateTime 2018/11/5 上午9:34
 */
@ApiModel
@Data
public class GoodsPageByConditionResponse implements Serializable {

    private static final long serialVersionUID = 8790632701356539648L;

    /**
     * 商品分页数据
     */
    @ApiModelProperty(value = "商品分页数据")
    private MicroServicePage<GoodsVO> goodsPage;
}
