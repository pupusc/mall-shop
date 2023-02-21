package com.wanmi.sbc.setting.tradeOrder.model.root;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table(name = "trade_order")
@Entity
public class GoodsMonth {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "GOODS_INFO_ID")
    private String goodsInfoId;

    @Column(name = "STAT_MONTH")
    private Integer statMonth;

    @Column(name = "PAY_COUNT")
    private BigDecimal payCount;

    @Column(name = "PAY_NUM")
    private BigDecimal payNum;

    @Column(name = "PAY_MONEY")
    private BigDecimal payMoney;

    @Column(name = "CREATE_TM")
    private LocalDateTime creatTM;
}
