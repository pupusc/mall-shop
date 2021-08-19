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
 * <p>限售配置修改结果</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的限售配置信息
     */
    @ApiModelProperty(value = "已修改的限售配置信息")
    private GoodsRestrictedSaleVO goodsRestrictedSaleVO;
}
