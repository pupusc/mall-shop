package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Liang Jun
 * @date 2022-02-22 20:21:00
 */
@Data
public class BaseReqVO {
    /**
     * 数据签名
     */
    @NotBlank
    private String sign;
}
