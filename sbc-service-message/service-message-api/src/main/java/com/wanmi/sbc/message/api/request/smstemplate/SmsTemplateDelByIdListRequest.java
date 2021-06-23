package com.wanmi.sbc.message.api.request.smstemplate;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除短信模板请求参数</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:43:29
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsTemplateDelByIdListRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-主键List
	 */
	@ApiModelProperty(value = "批量删除-主键List")
	@NotEmpty
	private List<Long> idList;
}