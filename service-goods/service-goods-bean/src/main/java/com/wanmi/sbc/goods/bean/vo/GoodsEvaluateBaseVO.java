package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;

/**
 * @author Liang Jun
 * @date 2022-08-03 13:22:00
 */
@Data
public class GoodsEvaluateBaseVO {
    /**
     * 客户端地址
     */
    private String clientIp;
    /**
     * 客户端位置
     */
    private String clientPlace;
}
