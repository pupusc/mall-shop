package com.wanmi.sbc.customer.api.request.detail;

import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>批量更新账户状态和禁用原因request</p>
 * Created by daiyitian on 2018-08-13-下午3:47.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerStateBatchModifyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -9155234181833842753L;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID列表")
    @NotEmpty
    private List<String> customerIds;

    /**
     * 账号状态
     */
    @ApiModelProperty(value = "账号状态")
    @NotNull
    private CustomerStatus customerStatus;

    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    private String forbidReason;

}
