package com.wanmi.sbc.goods.api.request.restrictedrecord;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除限售请求参数</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestrictedRecordDelByIdRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录主键
	 */
	@ApiModelProperty(value = "记录主键")
	@NotNull
	private Long recordId;
}