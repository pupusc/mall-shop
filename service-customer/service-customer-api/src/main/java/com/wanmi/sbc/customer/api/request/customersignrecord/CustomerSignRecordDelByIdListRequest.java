package com.wanmi.sbc.customer.api.request.customersignrecord;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除用户签到记录请求参数</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordDelByIdListRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-用户签到记录表idList
	 */
	@ApiModelProperty(value = "批量删除-用户签到记录表idList")
	@NotEmpty
	private List<String> signRecordIdList;
}