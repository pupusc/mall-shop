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
import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEmailDeleteRequest extends BaseRequest {

    private static final long serialVersionUID = -1011159642805870460L;
    /**
     * 客户邮箱Id
     */
    @ApiModelProperty(value = "客户邮箱Id")
    @NotBlank
    private String customerEmailId;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String delPerson;

    @Override
    public void checkParam() {
        // 客户邮箱id不能为空
        if (StringUtils.isBlank(this.customerEmailId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

//    public CustomerEmail generateCustomerEmail() {
//        CustomerEmail customerEmail = new CustomerEmail();
//        customerEmail.setCustomerEmailId(customerEmailId);
//        customerEmail.setDelFlag(DeleteFlag.YES);
//        customerEmail.setDelPerson(delPerson);
//        customerEmail.setDelTime(LocalDateTime.now());
//        return customerEmail;
//    }

}
