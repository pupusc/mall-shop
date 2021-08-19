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
 * 配送单明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistOrderItemOpen {

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 订单号
     */
    private String tid;

    /**
     * 发货商品数量
     */
    private Integer num;

    /**
     * 配送单号
     */
    private String dist_id;

    /**
     * 发货单号
     */
    private String delivery_no;

    /**
     * 店铺Id
     */
    private Long kdt_id;

    /**
     * 发货商品重量
     */
    private Long weight;

    /**
     * 订单商品id
     */
    private  String oid;
}
