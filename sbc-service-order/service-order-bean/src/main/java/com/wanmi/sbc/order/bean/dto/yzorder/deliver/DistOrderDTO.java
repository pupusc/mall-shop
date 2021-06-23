package com.wanmi.sbc.order.bean.dto.yzorder.deliver;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 配送单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistOrderDTO implements Serializable {

    private static final long serialVersionUID = -474950953546925076L;

    /**
     * 物流快递信息
     */
    private ExpressInfoDTO express_info;

    /**
     * 配送单明细
     */
    private List<DistOrderItemOpenDTO> dist_order_items;

    /**
     * 配送方式 1 快递 2 同城送 3 自提
     */
    private Integer dist_type;

    /**
     * 配送单状态 0 未发货 1 发货中 2 已发货 3 已取消
     */
    private Integer status;

    /**
     * 发货单号
     */
    private String delivery_no;

    /**
     * 主订单ID
     */
    private String tid;

    /**
     * 店铺Id
     */
    private Long kdt_id;

    /**
     * 扩展信息
     */
    private String extend;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime update_time;

    /**
     * 发货类型
     */
    private Integer delivery_type;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime create_time;

    /**
     * 出货点Id
     */
    private Long delivery_point_id;

    /**
     * 配送单id
     */
    private String dist_id;

}
