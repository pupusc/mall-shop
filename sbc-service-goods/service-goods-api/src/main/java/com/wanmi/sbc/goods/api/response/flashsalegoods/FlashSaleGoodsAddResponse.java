package com.wanmi.sbc.goods.api.response.flashsalegoods;

import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>抢购商品表新增结果</p>
 * @author bob
 * @date 2019-06-11 14:54:31
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleGoodsAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的抢购商品表信息
     */
    @ApiModelProperty(value = "已新增的抢购商品表信息")
    private List<FlashSaleGoodsVO> flashSaleGoodsVOS;
}
