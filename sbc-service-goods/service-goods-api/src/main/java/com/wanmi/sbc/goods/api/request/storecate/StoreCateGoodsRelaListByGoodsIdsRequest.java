package com.wanmi.sbc.goods.api.request.storecate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 10:00
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateGoodsRelaListByGoodsIdsRequest implements Serializable {

    private static final long serialVersionUID = 8375535033225134127L;

    @ApiModelProperty(value = "商品Id")
    @NotNull
    private List<String> goodsIds;
}
