package com.wanmi.sbc.setting.api.request.operatedatalog;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个删除系统日志请求参数</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateDataLogDelByOperateIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 操作id
	 */
	@ApiModelProperty(value = "操作id")
	@NotNull
	private String operateId;
}