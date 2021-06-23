package com.wanmi.sbc.goods.api.response.livegoods;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）直播商品信息response</p>
 * @author zwb
 * @date 2020-06-06 18:49:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播商品信息
     */
    @ApiModelProperty(value = "直播商品信息")
    private LiveGoodsVO liveGoodsVO;

    /**
     * 直播商品信息
     */
    @ApiModelProperty(value = "直播商品信息")
    private GoodsInfoVO goodsInfoVO;
}
