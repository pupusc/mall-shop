package com.wanmi.sbc.order.bean.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Liang Jun
 * @desc 外部交易平台
 * @date 2022-02-21 16:19:00
 */
@Getter
@AllArgsConstructor
public enum OutTradePlatEnum {
    FDDS_PERFORM("FDDS_PERFORM", "樊登读书-履约中台");
    private String code;
    private String desc;
}
