package com.wanmi.sbc.account.api.request.customerdrawcash;

import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * <p>单个删除会员提现管理请求参数</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashDelByIdRequest extends AccountBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 提现id
	 */
	@ApiModelProperty(value = "提现id")
	@NotNull
	private String drawCashId;
}