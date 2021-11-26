package com.wanmi.sbc.crm.api.request.customertagrel;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>会员标签关联列表查询请求参数</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagRelListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	private String customerId;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id", hidden = true)
	private Long tagId;

    /**
     * 显示标签名称
     */
    @ApiModelProperty(value = "显示标签名称", hidden = true)
	private Boolean showTagName;
}