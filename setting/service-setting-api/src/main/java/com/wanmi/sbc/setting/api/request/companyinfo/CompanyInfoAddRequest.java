package com.wanmi.sbc.setting.api.request.companyinfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>公司信息新增参数</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "公司ID")
	@Max(9223372036854775807L)
	private Long companyInfoId;
}