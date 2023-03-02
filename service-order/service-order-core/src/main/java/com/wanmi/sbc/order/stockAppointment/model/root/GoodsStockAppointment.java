package com.wanmi.sbc.order.stockAppointment.model.root;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "goods_stock_appointment")
public class GoodsStockAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Integer id;

    @Column(name = "customer")
    private String customer;

    @Column(name = "goods_info")
    private String goodsInfo;

    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "creat_time")
    private LocalDateTime creatTime;

    @Column(name = "account")
    private String account;

    @Column(name = "sku_name")
    private String skuName;

    @Column(name = "goods_id")
    private String goodsId;
}
