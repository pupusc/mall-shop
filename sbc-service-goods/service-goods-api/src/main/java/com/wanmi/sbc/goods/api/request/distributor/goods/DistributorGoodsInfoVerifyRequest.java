package com.wanmi.sbc.goods.api.request.distributor.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午2:21 2019/3/1
 * @Description:
 */
@ApiModel
@Data
public class DistributorGoodsInfoVerifyRequest {

    /**
     * 分销员id
     */
    @ApiModelProperty("分销员id")
    private String distributorId;

    /**
     * 单品id列表
     */
    @ApiModelProperty("单品id列表")
    private List<String> goodsInfoIds;

}
