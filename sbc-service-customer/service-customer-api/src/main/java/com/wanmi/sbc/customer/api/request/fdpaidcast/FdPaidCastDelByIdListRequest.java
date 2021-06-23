package com.wanmi.sbc.customer.api.request.fdpaidcast;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除樊登付费类型 映射商城付费类型请求参数</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdPaidCastDelByIdListRequest extends CustomerBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-樊登付费类型 映射商城付费类型主键List
	 */
	@ApiModelProperty(value = "批量删除-樊登付费类型 映射商城付费类型主键List")
	@NotEmpty
	private List<Long> idList;
}
