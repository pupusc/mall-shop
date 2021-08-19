package com.wanmi.sbc.goods.api.response.distributor.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: sbc-micro-service-B
 * @description:
 * @create: 2020-07-17 13:43
 **/
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorGoodsIdsResp implements Serializable {

    private static final long serialVersionUID = -4562013741000446619L;
    @ApiModelProperty("分销商品ID")
    private List<String> goodsIds;
}