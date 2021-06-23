package com.wanmi.sbc.customer.api.request.storesharerecord;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询商城分享请求参数</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreShareRecordByIdRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * shareId
	 */
	@ApiModelProperty(value = "shareId")
	@NotNull
	private Long shareId;
}