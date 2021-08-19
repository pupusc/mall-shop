package com.wanmi.sbc.goods.api.response.goodsrestrictedsale;

import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedSaleVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售配置列表结果</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 限售配置列表结果
     */
    @ApiModelProperty(value = "限售配置列表结果")
    private List<GoodsRestrictedSaleVO> goodsRestrictedSaleVOList;
}
