package com.wanmi.sbc.goods.adjust.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * <p>商品批量调价确认请求参数</p>
 * Created by of628-wenzhi on 2020-12-18-11:11 上午.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class PriceAdjustConfirmRequest extends BaseRequest {
    private static final long serialVersionUID = -5372213432454347648L;
    /**
     * 调价单号
     */
    @ApiModelProperty(value = "调价单号")
    @NotBlank
    private String adjustNo;

    /**
     * 是否立即生效
     */
    @ApiModelProperty(value = "是否立即生效")
    @NotNull
    private Boolean isNow;

    /**
     * 如果定时生效，生效时间不为空
     */
    @ApiModelProperty(value = "如果定时生效，生效时间不为空")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime startTime;

    @Override
    public void checkParam() {
        Validate.isTrue(isNow || Objects.nonNull(startTime), NULL_EX_MESSAGE,
                "startTime");
    }
}
