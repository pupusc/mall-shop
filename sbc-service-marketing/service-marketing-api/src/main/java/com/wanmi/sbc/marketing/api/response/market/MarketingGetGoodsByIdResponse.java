package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoResponseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-14 14:21
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingGetGoodsByIdResponse implements Serializable {

    private static final long serialVersionUID = 1728637538606821749L;
    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private GoodsInfoResponseVO goodsInfoResponseVO;

}
