package com.wanmi.sbc.setting.api.request.syssms;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除系统短信配置请求参数</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysSmsDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-主键List
	 */
	@ApiModelProperty(value = "批量删除-主键List")
	@NotEmpty
	private List<String> smsIdList;
}