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

import java.time.LocalDateTime;

/**
 * 新增客户邮箱
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEmailAddRequest extends BaseRequest {

    /**
     * 邮箱所属客户Id
     */
    @ApiModelProperty(value = "邮箱所属客户Id")
    private String customerId;

    /**
     * 发信人邮箱地址
     */
    @ApiModelProperty(value = "发信人邮箱地址")
    @NotEmpty
    @Length(max = 32)
    private String emailAddress;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Override
    public void checkParam() {
        if (StringUtils.isEmpty(this.emailAddress) || this.emailAddress.length() > 32) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * 客户邮箱对象
     *
     * @return
     */
//    public CustomerEmail generateCoupon() {
//        CustomerEmail customerEmail = new CustomerEmail();
//        customerEmail.setCustomerId(this.getCustomerId());
//        customerEmail.setEmailAddress(this.getEmailAddress());
//        customerEmail.setCreatePerson(this.getCreatePerson());
//        customerEmail.setCreateTime(LocalDateTime.now());
//        customerEmail.setDelFlag(DeleteFlag.NO);
//        return customerEmail;
//    }

}
