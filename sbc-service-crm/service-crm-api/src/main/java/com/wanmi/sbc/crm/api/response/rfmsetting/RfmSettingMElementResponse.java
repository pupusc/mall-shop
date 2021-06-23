package com.wanmi.sbc.crm.api.response.rfmsetting;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>rfm参数配置新增参数</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
public class RfmSettingMElementResponse extends RfmSettingRFMElementBaseResponse {
	private static final long serialVersionUID = 1L;

}