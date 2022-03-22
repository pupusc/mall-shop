package com.soybean.mall.order.miniapp.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mini_order_operate_result")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MiniOrderOperateResult implements Serializable {
    private static final long serialVersionUID = 7950081083998585970L;
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 操作类型1创建订单2支付结果通知3确认收货4售后
     */
    @Column(name = "operate_type")
    private Integer operateType;

    /**
     * 请求参数
     */
    @Column(name = "request_context")
    private String requestContext;

    /**
     * 返回结果
     */
    @Column(name = "result_context")
    private String resultContext;

    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private String orderId;


}
