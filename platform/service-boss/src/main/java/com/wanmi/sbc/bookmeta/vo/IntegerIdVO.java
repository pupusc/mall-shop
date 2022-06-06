package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Liang Jun
 * @date 2022-05-13 19:06:00
 */
@Data
public class IntegerIdVO {
    /**
     * 主键
     */
    @NotNull
    private Integer id;
}
