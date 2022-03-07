package com.wanmi.sbc.goods.info.model.root;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Proxy(lazy = false)
@Data
@Entity
@Table(name = "goods_special_price_sync")
public class GoodsSpecialPriceSync {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "goods_no")
    private String goodsNo;

    @Column(name = "special_price")
    private BigDecimal specialprice;

    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "deleted")
    private Integer deleted;
}
