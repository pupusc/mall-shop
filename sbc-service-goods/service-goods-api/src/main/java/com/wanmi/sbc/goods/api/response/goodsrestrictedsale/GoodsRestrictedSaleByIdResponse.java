package com.wanmi.sbc.goods.api.response.goodsrestrictedsale;

import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedSaleVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）限售配置信息response</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 限售配置信息
     */
    @ApiModelProperty(value = "限售配置信息")
    private GoodsRestrictedSaleVO goodsRestrictedSaleVO;
}
