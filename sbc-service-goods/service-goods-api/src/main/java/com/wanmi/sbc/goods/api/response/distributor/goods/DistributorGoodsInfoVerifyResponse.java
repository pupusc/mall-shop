package com.wanmi.sbc.goods.api.response.distributor.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:26 2019/3/1
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorGoodsInfoVerifyResponse {

    /**
     * 非小店精选单品列表
     */
    @ApiModelProperty("非小店精选单品列表")
    private List<String> invalidIds;

}
