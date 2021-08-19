package com.wanmi.sbc.order.yzorder.model.root;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "yz_order_record")
public class YzOrderRecord {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "tid")
    private String tid;

    @Column(name = "flag")
    private Boolean flag;
}
