package com.wanmi.sbc.elastic.api.request.groupon;



import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>根据活动ID批量更新审核状态</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsGrouponActivityRefuseRequest implements Serializable {


	private static final long serialVersionUID = -7903309035911953262L;
	/**
	 * 活动ID
	 */
	@ApiModelProperty(value = "审核拼团活动，grouponActivityId")
	@NotNull
	private String grouponActivityId;

	/**
	 * 分销拼团活动不通过原因
	 */
	@NotBlank
	@ApiModelProperty(value = "拼团活动不通过原因")
	private String auditReason;
}