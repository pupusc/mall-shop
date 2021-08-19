package com.wanmi.sbc.order.yzorder.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 交易基础信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1201746525643199062L;

    /**
     * 订单打标
     */
    private OrderTags order_tags;

    /**
     * 订单扩展信息
     */
    private OrderExtra order_extra;

    /**
     * 是否零售订单
     */
    private Boolean is_retail_order;

    /**
     * 支付类型
     */
    private Long pay_type;

    /**
     * 订单更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime update_time;

    /**
     * 多网点id，非多网点订单，返回为空。
     */
    private Long offline_id;

    /**
     * 订单发货时间（当所有商品发货后才会更新）
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime consign_time;

    /**
     * 订单号
     */
    private String tid;

    /**
     * 订单创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime created;

    /**
     * 主订单状态
     */
    private String status;

    /**
     * 总店id
     */
    private Long root_kdt_id;

    /**
     * 店铺类型 0:微商城; 1:微小店; 2:爱学贷微商城; 3:批发店铺; 4:批发商城; 5:外卖; 6:美业; 7:超级门店;
     * 8:收银; 9:收银加微商城; 10:零售总部; 99:有赞开放平台平台型应用创建的店铺
     */
    private Long team_type;

    /**
     * 订单过期时间（未付款将自动关单）
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime expired_time;

    /**
     * 退款状态 0:未退款; 1:部分退款中; 2:部分退款成功; 11:全额退款中; 12:全额退款成功
     */
    private Long refund_state;

    /**
     * 物流类型 0:快递发货; 1:到店自提; 2:同城配送; 9:无需发货（虚拟商品订单）
     */
    private Long express_type;

    /**
     * 主订单类型
     */
    private Long type;

    /**
     * 订单支付时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime pay_time;

    /**
     * 支付类型
     */
    private String pay_type_str;

    /**
     * 关闭类型
     */
    private Long close_type;

    /**
     * 门店id
     */
    private Long node_kdt_id;

    /**
     * 交易成功时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime success_time;

    /**
     * 订单确认时间（多人拼团成团）
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime confirm_time;

    /**
     * 详情页
     */
    private String order_url;

    /**
     * 主订单状态 描述
     */
    private String status_str;

    /**
     * 活动类型
     */
    private Integer activity_type;

}
