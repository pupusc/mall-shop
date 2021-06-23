package com.wanmi.sbc.pay.model.root;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 网关
 * Created by sunkun on 2017/8/3.
 */
@Entity
@Table(name = "pay_gateway")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayGateway implements Serializable {

    private static final long serialVersionUID = -1535631254507817260L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    /**
     * 商户id-boss端取默认值
     */
    @Column(name = "store_id")
    private Long storeId;

    /**
     * 网关名称
     */
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private PayGatewayEnum name;

    /**
     * 是否开启: 0关闭 1开启
     */
    @Column(name = "is_open")
    @Enumerated
    private IsOpen isOpen;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否聚合支付
     */
    @Column(name = "type")
    private Boolean type;

    /**
     * 支付网关配置
     */
    @JsonBackReference
    @OneToOne(mappedBy = "payGateway")
    private PayGatewayConfig config;

    /**
     * 支付项
     */
    @JsonBackReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gateway")
    private List<PayChannelItem> payChannelItemList;

    @Override
    public String toString() {
        return "PayGateway{" +
                "id=" + id +
                ", name=" + name +
                ", isOpen=" + isOpen +
                ", createTime=" + createTime +
                ", type=" + type +
                ", config=" + config +
                '}';
    }
}
