package com.wanmi.sbc.customer.points.model.root;

import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>会员积分明细实体类</p>
 */
@Data
@Entity
@Table(name = "customer_points_detail")
public class CustomerPointsDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 用户账号
     */
    @Column(name = "customer_account")
    private String customerAccount;

    /**
     * 用户名
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 操作类型 0:扣除 1:增长
     */
    @Column(name = "type")
    @Enumerated
    private OperateType type;

    /**
     * 会员积分业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买  5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信
     * 9添加收货地址 10关注店铺 11订单完成 12订单抵扣 13优惠券兑换 14积分兑换 15退单返还 16订单取消返还 17过期扣除
     */
    @Column(name = "service_type")
    @Enumerated
    private PointsServiceType serviceType;

    /**
     * 积分数量
     */
    @Column(name = "points")
    private Long points;

    /**
     * 内容备注
     */
    @Column(name = "content")
    private String content;

    /**
     * 积分余额
     */
    @Column(name = "points_available")
    private Long pointsAvailable;

    /**
     * 操作时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "op_time")
    private LocalDateTime opTime;

}