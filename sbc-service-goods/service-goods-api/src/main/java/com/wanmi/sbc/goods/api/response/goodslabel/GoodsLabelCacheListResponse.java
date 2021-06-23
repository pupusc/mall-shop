package com.wanmi.sbc.goods.api.response.goodslabel;

import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>商品标签缓存列表结果</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsLabelCacheListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品标签列表结果
     */
    @ApiModelProperty(value = "商品标签列表结果")
    private List<GoodsLabelVO> goodsLabelVOList;
}
