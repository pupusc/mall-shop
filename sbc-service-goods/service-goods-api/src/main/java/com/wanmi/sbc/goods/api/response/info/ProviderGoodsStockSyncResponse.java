package com.wanmi.sbc.goods.api.response.info;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p>供应商商品库存同步接口出参</p>
 * Created by of628-wenzhi on 2020-09-09-9:28 下午.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProviderGoodsStockSyncResponse implements Serializable {
    private static final long serialVersionUID = 8948702067291415259L;

    @ApiModelProperty("已同步库存商品sku列表")
    @NotEmpty
    private List<GoodsInfoVO> goodsInfoList;
}
