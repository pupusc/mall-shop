package com.wanmi.sbc.order.distribution.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: 消费记录
 * @Autho qiaokang
 * @Date：2019-03-04 16:20:52
 */
@Data
@Entity
@Table(name = "consume_record")
public class ConsumeRecord implements Serializable {

    private static final long serialVersionUID = 2592405729309444250L;
    /**
     * 主键:订单id
     */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 消费额
     */
    @Column(name = "consume_sum")
    private BigDecimal consumeSum;

    @Column(name ="flow_state")
    private String flowState;

    /**
     * 有效消费额
     */
    @Column(name = "valid_consume_sum")
    private BigDecimal validConsumeSum;

    /**
     * 店铺Id
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 分销员id
     */
    @Column(name = "distribution_customer_id")
    private String distributionCustomerId;

    /**
     * 下单时间
     */
    @Column(name = "order_create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime orderCreateTime;

    /**
     * 购买人的客户id
     */
    @Column(name="customer_id")
    private String customerId;

    /**
     * 客户姓名
     */
    @Column(name="customer_name")
    private String customerName;

    /**
     * 客户头像
     */
    @Column(name="head_img")
    private String headImg;
    /**
     * 是否是迁移数据
     */
    @Column(name="is_yz")
    private Integer isYz;

}
