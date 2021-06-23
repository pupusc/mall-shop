package com.wanmi.sbc.customer.api.request.paidcardbuyrecord;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除付费会员请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardBuyRecordDelByIdListRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-流水号List
	 */
	@ApiModelProperty(value = "批量删除-流水号List")
	@NotEmpty
	private List<String> payCodeList;
}