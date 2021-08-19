package com.wanmi.sbc.goods.api.response.info;

import com.wanmi.sbc.goods.bean.dto.GoodsInfoMarketingPriceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>批量查询SKU市场价返回数据结构</p>
 * Created by of628-wenzhi on 2020-12-14-9:30 下午.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class GoodsInfoMarketingPriceByNosResponse implements Serializable {
    private static final long serialVersionUID = -5028256287356066936L;
    /**
     * 商品市场价集合
     */
    @ApiModelProperty(value = "商品市场价集合")
    private List<GoodsInfoMarketingPriceDTO> dataList;
}
