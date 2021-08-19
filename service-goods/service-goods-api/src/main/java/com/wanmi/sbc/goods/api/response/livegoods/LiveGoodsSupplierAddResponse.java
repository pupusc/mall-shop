package com.wanmi.sbc.goods.api.response.livegoods;

import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>直播商品新增结果</p>
 * @author zwb
 * @date 2020-06-06 18:49:08
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveGoodsSupplierAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品列表
     */
    @ApiModelProperty(value = "商品列表")
    private List<LiveGoodsVO> goodsInfoVOList;

}
