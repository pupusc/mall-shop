package com.wanmi.sbc.goods.api.request.adjustprice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>确认调价请求参数</p>
 * Created by of628-wenzhi on 2020-12-17-11:54 上午.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class AdjustPriceConfirmRequest extends GoodsBaseRequest {

    private static final long serialVersionUID = 5400943981235559646L;
    /**
     * 调价单号
     */
    @ApiModelProperty(value = "调价单号")
    @NotBlank
    private String adjustNo;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    @NotNull
    private Long storeId;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime effectiveTime;

}
