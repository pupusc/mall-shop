package com.wanmi.sbc.customer.growthvalue.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>成长值明细表实体类</p>
 *
 * @author yang
 * @since 2019/2/22
 */
@Data
@Entity
@Table(name = "customer_growth_value")
public class CustomerGrowthValue implements Serializable {

    private static final long serialVersionUID = -3073747280335908732L;

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
     * 操作类型 0:扣除 1:增长
     */
    @Column(name = "type")
    @Enumerated
    private OperateType type;

    /**
     * 业务类型 0签到 1注册 2分享商品 3分享注册 4分享购买 5评论商品 6晒单 7上传头像/完善个人信息 8绑定微信 9添加收货地址 10关注店铺 11订单完成'
     */
    @Column(name = "service_type")
    @Enumerated
    private GrowthValueServiceType serviceType;

    /**
     * 成长值
     */
    @Column(name = "growth_value")
    private Long growthValue;

    /**
     * 操作时间
     */
    @Column(name = "op_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime opTime;

    /**
     * 相关单号
     */
    @Column(name = "trade_no")
    private String tradeNo;

    /**
     * 内容备注
     */
    @Column(name = "content")
    private String content;

    /**
     * 删除标记 0:未删除 1:已删除
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
