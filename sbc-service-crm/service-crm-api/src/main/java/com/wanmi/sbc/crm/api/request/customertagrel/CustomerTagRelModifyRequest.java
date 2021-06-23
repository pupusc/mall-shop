package com.wanmi.sbc.crm.api.request.customertagrel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * <p>会员标签关联修改参数</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagRelModifyRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 标签id
	 */
	@ApiModelProperty(value = "标签id")
	@NotNull
	@Max(9223372036854775807L)
	private Long tagId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max=50)
	private String createPerson;

}