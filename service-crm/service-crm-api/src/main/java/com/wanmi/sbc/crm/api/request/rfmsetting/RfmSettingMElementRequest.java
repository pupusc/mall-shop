package com.wanmi.sbc.crm.api.request.rfmsetting;

import com.wanmi.sbc.crm.bean.enums.Period;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

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
public class RfmSettingMElementRequest extends RfmSettingRFMElementBaseRequest {
	private static final long serialVersionUID = 1L;

}