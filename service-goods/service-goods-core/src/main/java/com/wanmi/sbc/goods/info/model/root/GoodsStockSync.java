package com.wanmi.sbc.goods.info.model.root;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Proxy(lazy = false)
@Data
@Entity
@Table(name = "goods_stock_sync")
public class GoodsStockSync {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "goods_no")
    private String goodsNo;

    @Column(name = "stock_change_time")
    private Date stockChangeTime;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "deleted")
    private Integer deleted;
}
