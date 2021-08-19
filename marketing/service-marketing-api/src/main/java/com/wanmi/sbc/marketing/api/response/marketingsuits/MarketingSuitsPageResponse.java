package com.wanmi.sbc.marketing.api.response.marketingsuits;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合商品主表分页结果</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 组合商品主表分页结果
     */
    @ApiModelProperty(value = "组合商品主表分页结果")
    private MicroServicePage<MarketingSuitsVO> marketingSuitsVOPage;
}
