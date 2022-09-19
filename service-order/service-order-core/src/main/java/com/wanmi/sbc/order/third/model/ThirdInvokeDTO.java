package com.wanmi.sbc.order.third.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/19 11:36 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_third_invoke")
public class ThirdInvokeDTO {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private String businessId;


    /**
     * 第三方业务id
     */
    @Column(name = "platform_id")
    private String platformId;

    /**
     * 分类 1 推送订单 2 推送售后单
     */
    @Column(name = "category")
    private Integer category;

    /**
     * 推送次数
     */
    @Column(name = "times")
    private Integer times;

    /**
     * 推送状态 1：初始状态，2：推送完成 3、推送失败
     */
    @Column(name = "push_status")
    private Integer pushStatus;

    /**
     * 返回结果
     */
    @Column(name = "result")
    private String result;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;


    @Column(name = "del_flag")
    private Integer delFlag;

}
