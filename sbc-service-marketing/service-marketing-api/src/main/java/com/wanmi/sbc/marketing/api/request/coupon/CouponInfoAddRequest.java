package com.wanmi.sbc.marketing.api.request.coupon;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.FullBuyType;
import com.wanmi.sbc.marketing.bean.enums.RangeDayType;
import com.wanmi.sbc.marketing.bean.enums.ScopeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 新增优惠券
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponInfoAddRequest extends BaseRequest {

    private static final long serialVersionUID = -9162622109556746710L;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    @NotBlank
    @Length(max = 10)
    private String couponName;

    /**
     * 起止时间类型 0：按起止时间，1：按N天有效
     */
    @ApiModelProperty(value = "起止时间类型")
    @NotNull
    private RangeDayType rangeDayType;

    /**
     * 有效天数
     */
    @ApiModelProperty(value = "有效天数")
    @Range(min = 1, max = 365)
    private Integer effectiveDays;

    /**
     * 购满多少钱
     */
    @ApiModelProperty(value = "购满多少钱")
    @Digits(integer = 99999, fraction = 0)
    @Range(min = 1, max = 99999)
    private BigDecimal fullBuyPrice;

    /**
     * 购满类型 0：无门槛，1：满N元可使用
     */
    @ApiModelProperty(value = "购满类型")
    @NotNull
    private FullBuyType fullBuyType;

    /**
     * 优惠券面值
     */
    @ApiModelProperty(value = "优惠券面值")
    @NotNull
    @Digits(integer = 99999, fraction = 0)
    @Range(min = 1, max = 99999)
    private BigDecimal denomination;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 营销类型(0,1,2,3,4) 0全部商品，1品牌，2平台(boss)类目,3店铺分类，4自定义货品（店铺可用）
     */
    @ApiModelProperty(value = "营销范围类型")
    @NotNull
    private ScopeType scopeType;


    @ApiModelProperty(value = "优惠券描述")
    private String couponDesc;

    /**
     * 优惠券类型 0通用券 1店铺券 2运费券
     */
    @ApiModelProperty(value = "优惠券类型")
    private CouponType couponType;

    /**
     * 促销范围Ids
     */
    @ApiModelProperty(value = "促销范围Id列表")
    private List<String> scopeIds;


    /**
     * 优惠券分类Ids
     */
    @ApiModelProperty(value = "优惠券分类Id列表")
    @Size(max = 3)
    private List<String> cateIds;

    /**
     * 是否平台优惠券 1平台 0店铺
     */
    @ApiModelProperty(value = "是否平台优惠券")
    private DefaultFlag platformFlag;

    /**
     * 商家Id  0：boss,  other:其他商家
     */
    @ApiModelProperty(value = "商家Id，0：boss, other:其他商家")
    private Long storeId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    @Override
    public void checkParam() {
        if (this.getRangeDayType().equals(RangeDayType.RANGE_DAY)) {
            if(Objects.isNull(this.getEndTime())){//起止时间非空验证
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            if(Objects.isNull(this.getStartTime())){//起止时间非空验证
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            if (this.getEndTime().isBefore(this.getStartTime())) {//起止时间
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        } else {
            if (Objects.isNull(this.getEffectiveDays())) {//n天内有效
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
        //使用门槛
        if (this.getFullBuyType().equals(FullBuyType.FULL_MONEY)) {
            if (Objects.isNull(this.getFullBuyPrice())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            } else {
                if (this.getFullBuyPrice().compareTo(denomination) <= 0) {//使用门槛值必须大于优惠券面值
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
                if (this.getFullBuyPrice().compareTo(BigDecimal.ONE) < 0 || fullBuyPrice.compareTo(BigDecimal.valueOf(99999)) > 0) {
                    throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                }
            }
        }
        //优惠券分类
        if (CollectionUtils.isNotEmpty(cateIds) && cateIds.size() > 3) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //优惠券商品
        if (!Objects.equals(ScopeType.ALL, this.getScopeType())) {
            if (CollectionUtils.isEmpty(scopeIds)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
    }
}
