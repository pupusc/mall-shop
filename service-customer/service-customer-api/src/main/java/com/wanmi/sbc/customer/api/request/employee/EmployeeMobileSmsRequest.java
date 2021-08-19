package com.wanmi.sbc.customer.api.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeMobileSmsRequest implements Serializable {

    private static final long serialVersionUID = -94434060715745422L;

    /**
     * 要发送短信的手机号码
     */
    @ApiModelProperty(value = "要发送短信的手机号码")
    @NotBlank
    private String mobile;

}
