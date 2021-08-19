package com.wanmi.sbc.pay.model.root;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 支付项
 * Created by sunkun on 2017/8/3.
 */
@Entity
@Table(name = "pay_channel_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PayChannelItem implements Serializable {

    private static final long serialVersionUID = -3086780909641511212L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 支付项名称
     */
    @Column(name = "name")
    private String name;



    /**
     * 支付渠道
     */
    @Column(name = "channel")
    private String channel;

    /**
     * 是否开启:0关闭 1开启
     */
    @Column(name = "is_open")
    @Enumerated
    private IsOpen isOpen;

    /**
     * 终端: 0 pc 1 h5  2 app
     */
    @Column(name = "terminal")
    private TerminalType terminal;

    /**
     * 支付项代码，同一支付网关唯一
     */
    @Column(name = "code")
    private String code;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;


    @JsonManagedReference
    @JoinColumn(name = "gateway_id")
    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private PayGateway gateway;

    /**
     * 网关名称
     */
    @Column(name = "gateway_name")
    @Enumerated(EnumType.STRING)
    private PayGatewayEnum gatewayName;

    @Override
    public String toString() {
        return "PayChannelItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", channel='" + channel + '\'' +
                ", isOpen=" + isOpen +
                ", terminal=" + terminal +
                ", code='" + code + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
