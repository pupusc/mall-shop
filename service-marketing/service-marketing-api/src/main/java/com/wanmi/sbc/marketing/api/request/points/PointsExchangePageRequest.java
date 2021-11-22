package com.wanmi.sbc.marketing.api.request.points;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PointsExchangePageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = 3761902374831057578L;


    @ApiModelProperty("sku编码")
    private List<String> skuList;

    @ApiModelProperty("spu编码")
    private List<String> spuList;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty( "活动名称")
    private String activityName;



}
