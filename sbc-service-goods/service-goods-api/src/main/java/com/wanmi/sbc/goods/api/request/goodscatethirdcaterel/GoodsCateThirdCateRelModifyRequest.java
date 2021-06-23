package com.wanmi.sbc.goods.api.request.goodscatethirdcaterel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>平台类目和第三方平台类目映射修改参数</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateThirdCateRelModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 平台类目主键
	 */
	@ApiModelProperty(value = "平台类目主键")
	@NotNull
	@Max(9223372036854775807L)
	private Long cateId;

	/**
	 * 第三方平台类目主键
	 */
	@ApiModelProperty(value = "第三方平台类目主键")
	@NotNull
	@Max(9223372036854775807L)
	private Long thirdCateId;

	/**
	 * 第三方渠道(0，linkedmall)
	 */
	@ApiModelProperty(value = "第三方渠道(0，linkedmall)")
	@NotNull
	@Max(127)
	private Integer thirdPlatformType;

	/**
	 * createTime
	 */
	@ApiModelProperty(value = "createTime", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}