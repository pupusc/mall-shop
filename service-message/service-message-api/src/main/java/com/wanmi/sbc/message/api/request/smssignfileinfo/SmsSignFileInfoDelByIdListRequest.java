package com.wanmi.sbc.message.api.request.smssignfileinfo;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除短信签名文件信息请求参数</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignFileInfoDelByIdListRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-主键List
	 */
	@ApiModelProperty(value = "批量删除-主键List")
	@NotEmpty
	private List<Long> idList;
}