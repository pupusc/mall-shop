package com.wanmi.sbc.order.bean.dto.yzorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 包裹信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistDTO implements Serializable {

    private static final long serialVersionUID = -5073780956742536276L;

    /**
     * 包裹id，由物流生成，用于唯一标识包裹
     */
    private String dist_id;

    /**
     * 包裹详情
     */
    private ExpressInfoDTO express_info;

    /**
     * 包裹中的商品列表
     */
    private List<DistItemDTO> dist_items;
}
