package com.wanmi.sbc.message.api.request.umengtoken;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除友盟推送设备与会员关系请求参数</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengTokenDelByIdListRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-idList
	 */
	@ApiModelProperty(value = "批量删除-idList")
	@NotEmpty
	private List<Long> idList;
}