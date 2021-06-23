package com.wanmi.sbc.goods.api.response.goodsrestrictedsale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>限售配置新增结果</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleValidateResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 限售校验的返回信息
     */
    @ApiModelProperty(value = "限售校验的返回信息")
    private Map<String,String> restrictValidateInfo;
}
