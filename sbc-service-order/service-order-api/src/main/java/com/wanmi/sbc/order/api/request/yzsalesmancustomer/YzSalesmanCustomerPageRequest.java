package com.wanmi.sbc.order.api.request.yzsalesmancustomer;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>有赞销售员客户关系分页查询请求参数</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzSalesmanCustomerPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * nickname
	 */
	@ApiModelProperty(value = "nickname")
	private String nickname;

	/**
	 * ctUid
	 */
	@ApiModelProperty(value = "ctUid")
	private Long ctUid;

	/**
	 * isValid
	 */
	@ApiModelProperty(value = "isValid")
	private Integer isValid;

	/**
	 * avatar
	 */
	@ApiModelProperty(value = "avatar")
	private String avatar;

	/**
	 * ctYzOpenId
	 */
	@ApiModelProperty(value = "ctYzOpenId")
	private String ctYzOpenId;

	/**
	 * relationId
	 */
	@ApiModelProperty(value = "relationId")
	private Long relationId;

	/**
	 * allChannelName
	 */
	@ApiModelProperty(value = "allChannelName")
	private String allChannelName;

}