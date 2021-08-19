package com.wanmi.sbc.customer.api.response.points;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>会员积分过期提醒response</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsExpireResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "客户ID")
    private String customerId;

    @ApiModelProperty(value = "积分过期开关")
    private EnableStatus pointsExpireStatus;

    @ApiModelProperty(value = "积分过期时间")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate pointsExpireDate;

    @ApiModelProperty(value = "即将过期积分数")
    private Long willExpirePoints;
}
