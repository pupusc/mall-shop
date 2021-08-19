package com.wanmi.sbc.customer.api.request.detail;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>根据多个会员id删除会员明细request</p>
 * Created by daiyitian on 2018-08-13-下午3:47.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDetailDeleteRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -9155234181833842753L;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID列表")
    @NotEmpty
    private List<String> customerIds;

}
