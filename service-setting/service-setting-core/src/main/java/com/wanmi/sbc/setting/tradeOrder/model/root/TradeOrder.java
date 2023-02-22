package com.wanmi.sbc.setting.tradeOrder.model.root;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "trade_order")
@Entity
public class TradeOrder {

    @Id
    @Column(name = "trade_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String tradeOrderId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_account")
    private String customerAccount;

    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "store_id")
    private Integer storeId;

    @Column(name = "audit_state")
    private String auditState;

    @Column(name = "flow_state")
    private String flowState;

    @Column(name = "pay_state")
    private String payState;

    @Column(name = "deliver_status")
    private String deliverStatus;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "oid")
    private String oId;

    @Column(name = "spu_id")
    private String spuId;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "sku_name")
    private String skuName;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "cate_id")
    private Integer cateId;

    @Column(name = "brand_id")
    private Integer brandId;
}
