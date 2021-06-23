package com.wanmi.sbc.marketing.coupon.model.root;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 优惠券活动关联配置表
 */
@Entity
@Table(name = "coupon_cate_rela")
@Data
public class CouponCateRela {

    /**
     *  优惠券活动关联配置表id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "cate_rela_id")
    private String cateRelaId;

    /**
     *  优惠券id
     */
    @Column(name = "coupon_id")
    private String couponId ;

    /**
     *  分类id
     */
    @Column(name = "cate_id")
    private String cateId ;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @Column(name = "platform_flag")
    @Enumerated
    private DefaultFlag platformFlag;

}
