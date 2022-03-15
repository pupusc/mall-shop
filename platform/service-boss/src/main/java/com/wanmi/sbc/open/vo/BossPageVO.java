package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Liang Jun
 * @date 2022-02-15 14:35:00
 */
@Data
public class BossPageVO {
    @NotNull
    private Integer pageNo;
    @NotNull
    private Integer pageSize;
    private Integer totalCount;
}
