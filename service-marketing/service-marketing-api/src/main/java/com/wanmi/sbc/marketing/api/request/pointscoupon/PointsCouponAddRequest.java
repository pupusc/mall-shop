package com.wanmi.sbc.marketing.api.request.pointscoupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>积分兑换券表新增参数</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsCouponAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    @NotBlank
    private String couponId;

    /**
     * 兑换总数
     */
    @ApiModelProperty(value = "兑换总数")
    @NotNull
    private Long totalCount;

    /**
     * 已兑换数量
     */
    @ApiModelProperty(value = "已兑换数量")
    @NotNull
    private Long exchangeCount;

    /**
     * 兑换积分
     */
    @ApiModelProperty(value = "兑换积分")
    @NotNull
    private Long points;

    /**
     * 是否售罄
     */
    @ApiModelProperty(value = "是否售罄")
    private BoolFlag sellOutFlag;

    /**
     * 是否启用 0：停用，1：启用
     */
    @ApiModelProperty(value = "是否启用 0：停用，1：启用")
    @NotNull
    private EnableStatus status;

    /**
     * 兑换开始时间
     */
    @ApiModelProperty(value = "兑换开始时间")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTime;

    /**
     * 兑换结束时间
     */
    @ApiModelProperty(value = "兑换结束时间")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 删除标识,0: 未删除 1: 已删除
     */
    @ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
    private DeleteFlag delFlag;

}