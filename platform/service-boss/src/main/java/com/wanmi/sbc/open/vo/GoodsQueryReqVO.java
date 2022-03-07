package com.wanmi.sbc.open.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Liang Jun
 * @date 2022-02-15 14:14:00
 */
@Data
public class GoodsQueryReqVO extends BaseReqVO {
    private String skuNo;
    private String goodsName;
    @NotNull
    private BossPageVO page;
}
