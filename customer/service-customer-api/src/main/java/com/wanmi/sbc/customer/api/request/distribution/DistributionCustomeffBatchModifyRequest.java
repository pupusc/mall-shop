package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.common.enums.DefaultFlag;
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
public class DistributionCustomeffBatchModifyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -9155234181833842753L;

    /**
     * 分销员ID
     */
    @ApiModelProperty(value = "分销员ID列表")
    @NotEmpty
    private List<String> distributionIds;

    /**
     * 是否禁止分销 0: 启用中  1：禁用中
     */
    @ApiModelProperty(value = "是否禁止分销 0: 启用中  1：禁用中")
    @NotNull
    private DefaultFlag forbiddenFlag;

    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    private String forbiddenReason;

}
