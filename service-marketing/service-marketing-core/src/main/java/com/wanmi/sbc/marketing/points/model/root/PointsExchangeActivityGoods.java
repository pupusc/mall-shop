package com.wanmi.sbc.marketing.points.model.root;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "points_exchange_activity_goods")
public class PointsExchangeActivityGoods {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "activity_id")
    private Integer activityId;
    @Column(name = "sku_no")
    private String skuNo;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "cash")
    private BigDecimal cash;
    @Column(name = "points")
    private Integer points;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "deleted")
    private Integer deleted;

}
