package com.wanmi.sbc.order.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 物流记录-简化版
 * Created by dyt on 2020/4/17.
 */
@Data
public class LogisticsLogSimpleVO implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "商品图片")
    private String goodsImg;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回等7个状态")
    private String state;

    @ApiModelProperty(value = "内容")
    private String context;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "本地发货单号")
    private String deliverId;
}
