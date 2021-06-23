package com.wanmi.sbc.order.api.response.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 流水单
 * Created by zhangjin on 2017/4/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RefundBillDeleteByIdResponse implements Serializable{

    private static final long serialVersionUID = 8530724458639491058L;

    @ApiModelProperty(value = "删除结果")
    private Integer result;
}
