package com.wanmi.sbc.message.api.request.smssign;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除短信签名请求参数</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignDelByIdRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@NotNull
	private Long id;
}