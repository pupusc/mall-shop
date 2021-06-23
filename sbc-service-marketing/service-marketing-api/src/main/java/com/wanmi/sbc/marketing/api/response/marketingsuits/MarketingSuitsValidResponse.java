package com.wanmi.sbc.marketing.api.response.marketingsuits;

import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据id查询任意（包含已删除）组合商品主表信息response</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsValidResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 营销视图对象列表
     */
    @ApiModelProperty(value = "营销视图对象列表")
    private MarketingVO marketingVO;

    /**
     * 组合商品主表信息
     */
    @ApiModelProperty(value = "组合商品主表信息")
    private MarketingSuitsVO marketingSuitsVO;


    /**
     * 组合活动关联商品sku表列表结果
     */
    @ApiModelProperty(value = "组合活动关联商品sku表列表结果")
    private List<MarketingSuitsSkuVO> marketingSuitsSkuVOList;



}
