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
 * 发货信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YzOrderDeliverDTO implements Serializable {

    private static final long serialVersionUID = 5538690311990049253L;

    /**
     * 发货单数据唯一标识
     */
    private Long id;

    /**
     * 货单号，微商城订单发货时产生，零售订单完成派单时产生
     */
    private String delivery_no;

    /**
     * 发货模式，0-手动发货，1-系统自动发货
     */
    private Integer delivery_mode;

    /**
     * 发货模式描述：手动发货、系统自动发货
     */
    private String delivery_mode_desc;

    /**
     * 应付运费
     */
    private Long delivery_fee;

    /**
     * 实付运费
     */
    private Long real_delivery_fee;

    /**
     * 出货点Id
     */
    private Long delivery_point_id;

    /**
     * 备货状态
     */
    private Integer stock_status;

    /**
     * 扩展信息
     */
    private String extend;

    /**
     * 发货时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime create_time;

    /**
     * 店铺在有赞的id标识，有赞平台生成，在有赞平台唯一，用于判断信息属于哪一个店铺
     */
    private Long kdt_id;

    /**
     * 发货流程类型,1: 基于订单发货流程, 2:基于货单发货流程, 3:货单升级, 4: 多期订单发货流程, 5:多地址发货流程, 6:单订单-单商品-多运单发货流程
     */
    private Integer flow_type;

    /**
     * 出货点类型，0: 门店, 1: 仓库, 2: 总店, 3: 网点
     */
    private Integer delivery_point_type;

    /**
     * 单发货状态，0-待发货， 1-已发货， 2-无需发货
     */
    private Integer status;

    /**
     * 货单状态描述：待发货、已发货、无需物流
     */
    private String status_desc;

    /**
     * 货单备注
     */
    private String remark;

    /**
     * 发货来源信息，标识发货操作来源于客户端还是open接口调用
     */
    private String source;

    /**
     * 配送方式 1-快递 2-同城送 3-自提
     */
    private Integer dist_type;

    /**
     * 配送方式描述：快递、同城送、自提
     */
    private String dist_type_desc;

    /**
     * 货单更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime update_time;

    /**
     * 货单明细
     */
    private List<DeliverOrderItemDTO> delivery_order_items;

    /**
     * 配送单
     */
    private List<DistOrderDTO> dist_order_d_t_os;


}
