package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 订单发货详情结构体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderDTO implements Serializable {

    private static final long serialVersionUID = 7231768923140698460L;

    /**
     * 物流状态 0:待发货; 1:已发货
     */
    private Integer express_state;

    /**
     * 包裹id
     */
    private Long pk_id;

    /**
     * 发货方式。 0:手动发货（商城后台人工发货），1:接口发货（有赞云发货API发货）
     */
    private Integer express_type;

    /**
     * 包裹信息
     */
    private List<DistDTO> dists;

    /**
     * 发货明细
     */
    private List<OidDTO> oids;

}
