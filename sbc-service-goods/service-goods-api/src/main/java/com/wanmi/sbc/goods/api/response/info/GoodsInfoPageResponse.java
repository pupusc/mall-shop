package com.wanmi.sbc.goods.api.response.info;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品SKU分页响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
public class GoodsInfoPageResponse implements Serializable {

    private static final long serialVersionUID = -4370164109574914820L;

    /**
     * 分页商品SKU信息
     */
    @ApiModelProperty(value = "分页商品SKU信息")
    private MicroServicePage<GoodsInfoVO> goodsInfoPage;
}
