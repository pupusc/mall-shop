package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>券码关联订单参数--发券</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodeLinkOrderRequest {


    @ApiModelProperty("订单号")
    private String tid;

    /**
     * 卡券ID列表
     * <p>
     * 如果卡券ID列表里有多个相同的ID 表示多次发券
     */
    @ApiModelProperty("卡券ID列表")
    private List<Long> couponIds;

    @ApiModelProperty("更新人")
    private String updatePerson;

}