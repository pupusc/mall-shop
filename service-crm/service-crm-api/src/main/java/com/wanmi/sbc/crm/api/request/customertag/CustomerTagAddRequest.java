package com.wanmi.sbc.crm.api.request.customertag;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>会员标签新增参数</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTagAddRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签名称
	 */
	@ApiModelProperty(value = "标签名称")
	@Length(min = 1,max=20)
	private String name;

	/**
	 * 会员人数
	 */
	@ApiModelProperty(value = "会员人数")
	@Max(9999999999L)
	private Integer customerNum;

	/**
	 * 删除标志位，0:未删除，1:已删除
	 */
	@ApiModelProperty(value = "删除标志位，0:未删除，1:已删除")
	private DeleteFlag delFlag;

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