package com.wanmi.sbc.customer.api.request.paidcardrightsrel;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员列表查询请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardRightsRelListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<String> idList;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 所属会员权益id
	 */
	@ApiModelProperty(value = "所属会员权益id")
	private String paidCardId;

	/**
	 * 权益id
	 */
	@ApiModelProperty(value = "权益id")
	private Integer rightsId;

}