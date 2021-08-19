package com.wanmi.sbc.order.api.request.trade;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 11:37
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnOrderModifyAutoAuditRequest implements Serializable {
    private static final long serialVersionUID = 777410875769675027L;

    @ApiModelProperty(value = "天")
    private Integer day;
}
