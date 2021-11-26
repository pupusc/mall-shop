package com.wanmi.sbc.crm.api.request.customerplan;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import com.wanmi.sbc.crm.bean.enums.TriggerCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * <p> 人群运营计划修改参数</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanModifyPauseFlagRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标识
	 */
	@ApiModelProperty(value = "标识")
	@NotNull
	private Long id;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updatePerson;

    /**
     * 是否暂停 0:开启1:暂停
     */
    @ApiModelProperty(value = "是否暂停 0:开启1:暂停")
    @NotNull
    private Boolean pauseFlag;

}