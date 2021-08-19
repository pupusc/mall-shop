package com.wanmi.sbc.marketing.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CouponActivityType;
import lombok.Data;

import javax.persistence.Enumerated;
import java.time.LocalDateTime;

/**
 * @Author: songhanlin
 * @Date: Created In 11:15 AM 2018/9/12
 * @Description: 优惠券活动表
 */
@Data
public class CouponActivityDTO {

    /**
     * 优惠券活动id
     */

    private String activityId;

    /**
     * 优惠券活动名称
     */
    private String activityName;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 优惠券类型，0全场赠券，1指定赠券，2进店赠券，3注册赠券， 4权益赠券
     */
    @Enumerated
    private CouponActivityType couponActivityType;

    /**
     * 领取类型，0 每人限领次数不限，1 每人限领N次
     */
    @Enumerated
    private DefaultFlag receiveType;

    /**
     * 是否暂停 ，1 暂停
     */
    @Enumerated
    private DefaultFlag pauseFlag;

    /**
     * 优惠券被使用后可再次领取的次数，每次仅限领取1张
     */
    private Integer receiveCount;

    /**
     * 生效终端，逗号分隔 0全部,1.PC,2.移动端,3.APP
     */
    private String terminals;

    /**
     * 商户id
     */
    private Long storeId;

    /**
     * 是否平台 0店铺 1平台
     */
    @Enumerated
    private DefaultFlag platformFlag;

    /**
     * 是否删除标志 0：否，1：是
     */
    @Enumerated
    private DeleteFlag delFlag;

    /**
     * 关联的客户等级  -5付费会员 -4企业会员 -3指定人群 -2指定客户 -1:全部客户 0:全部等级 other:其他等级 ,
     */
    private String joinLevel;


    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
     */
    private DefaultFlag joinLevelType;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 修改时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    private String updatePerson;

    /**
     * 删除时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    /**
     * 删除人
     */
    private String delPerson;

    /**
     * 剩余优惠券组数
     */
    private Integer leftGroupNum;

    /**
     * 参与成功通知标题
     */
    private String activityTitle;

    /**
     * 参与成功通知描述
     */
    private String activityDesc;
}
