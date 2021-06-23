package com.wanmi.sbc.marketing.api.response.marketingsuits;

import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>组合商品主表新增结果</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的组合商品主表信息
     */
    @ApiModelProperty(value = "已新增的组合商品主表信息")
    private MarketingSuitsVO marketingSuitsVO;
}
