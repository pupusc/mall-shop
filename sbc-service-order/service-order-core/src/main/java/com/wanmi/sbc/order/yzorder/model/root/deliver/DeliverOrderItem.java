package com.wanmi.sbc.order.yzorder.model.root.deliver;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 货单明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliverOrderItem {

    /**
     * 商品发货状态：待发货、已发货、无需物流
     */
    private String delivery_status_desc;

    /**
     * 商品发货状态
     */
    private Integer delivery_status;

    /**
     * 店铺Id
     */
    private Long kdt_id;

    /**
     * 订单商品id，即子订单号
     */
    private Long item_id;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime create_time;

    /**
     * 发货商品重量 单位：克（g)
     */
    private Long weight;

    /**
     * 订单号
     */
    private String tid;

    /**
     * 发货商品数量
     */
    private Integer num;

    /**
     * 货单号
     */
    private String delivery_no;
}
