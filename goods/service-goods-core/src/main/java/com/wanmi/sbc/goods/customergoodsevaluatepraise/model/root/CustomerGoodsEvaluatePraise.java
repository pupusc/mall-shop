package com.wanmi.sbc.goods.customergoodsevaluatepraise.model.root;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>会员商品评价点赞关联表实体类</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@Data
@Entity
@Table(name = "customer_goods_evaluate_praise")
public class CustomerGoodsEvaluatePraise implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    /**
     * 会员id
     */
    @Column(name = "customer_id")
    private String customerId;

    /**
     * 商品评价id
     */
    @Column(name = "goods_evaluate_id")
    private String goodsEvaluateId;

    /**
     * 创建时间
     */
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

}