package com.wanmi.sbc.order.bean.dto.yzorder.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 物流信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpressInfoDTO implements Serializable {

    private static final long serialVersionUID = 512454789928571102L;

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
    private ExpressDetailDTO express_detail;

}
