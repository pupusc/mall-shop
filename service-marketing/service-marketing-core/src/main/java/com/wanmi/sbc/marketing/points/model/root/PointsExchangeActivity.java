package com.wanmi.sbc.marketing.points.model.root;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "points_exchange_activity")
public class PointsExchangeActivity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ApiModelProperty("活动名称")
    @Column(name = "activity_name")
    private String activityName;
    @ApiModelProperty("开始时间")
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @ApiModelProperty("积分数量")
    @NotNull
    @Column(name = "points")
    private Integer points;
    @Column(name = "customerType")
    @ApiModelProperty("目标客户类型0全平台1付费2企业")
    private Integer customer_type;
    @Column(name = "customer_level")
    @ApiModelProperty("客户登记0全部登记1银杏级，用，分割")
    private String customerLevel;
    @Column(name = "status")
    private Integer status;

    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "deleted")
    private Integer deleted;

}
