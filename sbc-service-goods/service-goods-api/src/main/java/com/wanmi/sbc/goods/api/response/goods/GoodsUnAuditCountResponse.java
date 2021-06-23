package com.wanmi.sbc.goods.api.response.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.response.goods.GoodsUnAuditCountResponse
 * 待审核商品统计响应对象
 * @author lipeng
 * @dateTime 2018/11/5 上午11:22
 */
@ApiModel
@Data
public class GoodsUnAuditCountResponse implements Serializable {

    private static final long serialVersionUID = -4800054636660869217L;

    @ApiModelProperty(value = "待审核商品统计数量")
    private Long unAuditCount;
}
