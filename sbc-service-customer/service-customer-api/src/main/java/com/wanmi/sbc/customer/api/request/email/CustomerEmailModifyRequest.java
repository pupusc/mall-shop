package com.wanmi.sbc.customer.api.request.email;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEmailModifyRequest extends BaseRequest {

    /**
     * 邮箱配置Id
     */
    @ApiModelProperty(value = "邮箱配置Id")
    @NotNull
    private String customerEmailId;

    /**
     * 邮箱所属客户Id
     */
    @ApiModelProperty(value = "邮箱所属客户Id")
    @NotNull
    private String customerId;

    /**
     * 发信人邮箱地址
     */
    @ApiModelProperty(value = "发信人邮箱地址")
    @NotEmpty
    @Length(max = 32)
    private String emailAddress;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @Override
    public void checkParam() {
        if (this.customerEmailId == null || this.customerId == null
                || StringUtils.isEmpty(this.emailAddress) || this.emailAddress.length() > 32) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

}
