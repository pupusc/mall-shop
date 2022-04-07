package com.wanmi.sbc.feishu;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeiShuNoticeEnum {

    STOCK("库存"),
    COST_PRICE("成本价");

    private String message;
}
