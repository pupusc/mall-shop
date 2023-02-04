package com.wanmi.sbc.goods.SuspensionV2.model;


import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DeleteFlag;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <p>自动标签实体类</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@Data
@Entity
@Table(name = "t_window")
public class Suspension  {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 跳转id
     */
    @Column(name = "to_id")
    private String toId;

    /**
     * 跳转url
     */
    @Column(name = "to_href")
    private String toHref;

    /**
     * 排序
     */
    @Column(name = "order_num")
    private Integer orderNum;

    /**
     * 是否启用
     */
    @Column(name = "publish_state")
    private Boolean publishState;

    /**
     * 投放开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 投放结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
}
