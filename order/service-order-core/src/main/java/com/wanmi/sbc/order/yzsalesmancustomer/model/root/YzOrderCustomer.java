package com.wanmi.sbc.order.yzsalesmancustomer.model.root;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "yz_order_customer")
public class YzOrderCustomer implements Serializable {


    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * yz_uid
     */
    @Column(name = "yz_uid")
    private Long yzUid;

    /**
     * yz_openId
     */
    @Column(name = "yz_open_id")
    private String yzOpenId;

    /**
     * flag(是否补偿完成0：未开始 1：已完成)
     */
    @Column(name = "flag")
    private Integer flag;

}
