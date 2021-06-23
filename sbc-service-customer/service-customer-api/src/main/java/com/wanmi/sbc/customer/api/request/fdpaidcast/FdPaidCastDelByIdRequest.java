package com.wanmi.sbc.customer.api.request.fdpaidcast;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除樊登付费类型 映射商城付费类型请求参数</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdPaidCastDelByIdRequest extends CustomerBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * 樊登付费类型 映射商城付费类型主键
     */
    @ApiModelProperty(value = "樊登付费类型 映射商城付费类型主键")
    @NotNull
    private Long id;
}
