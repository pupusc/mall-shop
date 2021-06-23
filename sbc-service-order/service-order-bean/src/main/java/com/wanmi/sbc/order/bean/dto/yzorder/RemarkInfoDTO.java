package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 标记信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarkInfoDTO implements Serializable {

    private static final long serialVersionUID = -5654034990636608982L;

    /**
     * 订单买家留言
     */
    private String buyer_message;

    /**
     * 订单标星等级 0-5
     */
    private Long star;

    /**
     * 订单商家备注
     */
    private String trade_memo;
}
