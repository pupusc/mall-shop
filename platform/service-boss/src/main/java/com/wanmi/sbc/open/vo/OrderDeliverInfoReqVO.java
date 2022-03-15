package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Liang Jun
 * @date 2022-02-16 10:03:00
 */
@Data
public class OrderDeliverInfoReqVO extends BaseReqVO {
    @NotBlank
    private String orderNo;
}
