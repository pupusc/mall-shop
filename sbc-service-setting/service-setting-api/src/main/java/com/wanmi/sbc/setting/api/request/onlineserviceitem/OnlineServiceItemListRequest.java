package com.wanmi.sbc.setting.api.request.onlineserviceitem;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>onlineerviceItem列表查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceItemListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-在线客服座席idList
	 */
	@ApiModelProperty(value = "批量查询-在线客服座席idList")
	private List<Integer> serviceItemIdList;

	/**
	 * 在线客服座席id
	 */
	@ApiModelProperty(value = "在线客服座席id")
	private Integer serviceItemId;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	private Long storeId;

	/**
	 * 在线客服主键
	 */
	@ApiModelProperty(value = "在线客服主键")
	private Integer onlineServiceId;

}