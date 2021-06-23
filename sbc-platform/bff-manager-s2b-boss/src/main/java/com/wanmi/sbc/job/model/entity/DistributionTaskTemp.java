package com.wanmi.sbc.job.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by hht on 2017/12/4.
 */
@Data
@Entity
@Table(name = "distribution_task_temp")
public class DistributionTaskTemp {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * 购买人id
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 推荐分销员id
     */
    @Column(name = "distribution_customer_id")
    private String distributionCustomerId;

    /**
     * 第一次有效购买
     */
    @Column(name = "first_valid_buy")
    private BoolFlag firstValidBuy;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单失效时间
     */
    @Column(name = "order_disable_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderDisableTime;

    /**
     * 分销订单
     */
    @Column(name = "distribution_order")
    private BoolFlag distributionOrder;

    /**
     * 退单中的数量
     */
    @Column(name = "return_order_num")
    private Integer returnOrderNum;

}
