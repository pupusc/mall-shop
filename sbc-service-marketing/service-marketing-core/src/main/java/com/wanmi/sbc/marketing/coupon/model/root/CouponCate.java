package com.wanmi.sbc.marketing.coupon.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_cate")
@Data
public class CouponCate {
    /**
     *  优惠券分类Id
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "coupon_cate_id")
    private String couponCateId;

    /**
     *  优惠券分类名称
     */
    @Column(name = "coupon_cate_name")
    private String couponCateName;

    /**
     *  排序
     */
    @Column(name = "cate_sort")
    private Integer cateSort;

    /**
     *  是否平台专用 0：否，1：是
     */
    @Column(name = "only_platform_flag")
    @Enumerated
    private DefaultFlag onlyPlatformFlag;

    /**
     *  是否删除标志 0：否，1：是
     */
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;

    /**
     *  创建时间
     */
    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     *  创建人
     */
    @Column(name = "create_person")
    private String createPerson;

    /**
     *  修改时间
     */
    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     *  修改人
     */
    @Column(name = "update_person")
    private String updatePerson;

    /**
     *  删除时间
     */
    @Column(name = "del_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    /**
     *  删除人
     */
    @Column(name = "del_person")
    private String delPerson;
}
