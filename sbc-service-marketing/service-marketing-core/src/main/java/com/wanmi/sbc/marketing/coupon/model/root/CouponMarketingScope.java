package com.wanmi.sbc.marketing.coupon.model.root;

import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author: lq
 * @CreateTime:2018-09-12 09:34
 * @Description:优惠券商品作用范围
 */
@Data
@Entity
@Table(name = "coupon_marketing_scope")
public class CouponMarketingScope {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "marketing_scope_id")
    private String marketingScopeId;

    /**
     * 优惠券id
     */
    @Column(name = "coupon_id")
    private String couponId;


    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @Column(name = "scope_type")
    @Enumerated
    private ScopeType scopeType;

    /**
     * 分类层级
     */
    @Column(name = "cate_grade")
    private Integer cateGrade;

    /**
     * 营销id,可以为0(全部)，brand_id(品牌id)，cate_id(分类id), goods_info_id(货品id)
     */
    @Column(name = "scope_id")
    private String scopeId;


}
