package com.wanmi.sbc.order.yzorder.model.root.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressInfo {

    /**
     * 物流公司id
     */
    private Integer express_id;

    /**
     * 物流单号
     */
    private String express_no;

    /**
     * 物流详情
     */
    private ExpressDetail express_detail;

}
