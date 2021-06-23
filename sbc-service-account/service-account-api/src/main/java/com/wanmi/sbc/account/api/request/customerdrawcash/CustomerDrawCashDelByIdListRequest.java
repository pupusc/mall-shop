package com.wanmi.sbc.account.api.request.customerdrawcash;

import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;

/**
 * <p>批量删除会员提现管理请求参数</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashDelByIdListRequest extends AccountBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-提现idList
	 */
	@ApiModelProperty(value = "批量删除-提现idList")
	@NotEmpty
	private List<String> drawCashIdList;
}