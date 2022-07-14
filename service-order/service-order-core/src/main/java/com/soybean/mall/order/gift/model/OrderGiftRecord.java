package com.soybean.mall.order.gift.model;

import com.wanmi.sbc.common.enums.DeleteFlag;
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
 * Date       : 2022/7/13 8:02 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_order_gift_record")
public class OrderGiftRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 客户id
     */
    @Column(name = "customer_id")
    private String customerId;


    /**
     * 订单id
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 活动id
     */
    @Column(name = "activity_id")
    private String activityId;

    /**
     * 份数
     */
    @Column(name = "per")
    private Integer per;

    /**
     * 外部引用id
     */
    @Column(name = "quote_id")
    private String quoteId;

    /**
     * 外部引用表名称
     */
    @Column(name = "quoteName")
    private String quoteName;

    /**
     * 记录分类 1 返积分活动
     */
    @Column(name = "record_category")
    private Integer recordCategory;

    /**
     * 记录状态 0、创建 1 锁定 2 成功 3 普通取消 4 黑名单取消
     */
    @Column(name = "record_status")
    private Integer recordStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Column(name = "del_flag")
    private DeleteFlag delFlag;

}
