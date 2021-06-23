package com.wanmi.sbc.marketing.api.response.grouponactivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:24 2019/5/17
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityListSpuIdResponse {

    /**
     * 正在进行的活动的商品ids
     */
    @ApiModelProperty(value = "正在进行的活动的商品ids")
    private List<String> goodsIds;

}
