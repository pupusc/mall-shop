package com.wanmi.sbc.order.bean.dto.yzorder.deliver;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 多期发货记录配送信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiPeriodDistInfoDTO implements Serializable {

    private static final long serialVersionUID = 5820824303570892731L;

    /**
     * 是否允许修改物流
     */
    private Boolean allow_modify_express;

    /**
     * 配送单
     */
    private DistOrderDTO dist_order;
}
