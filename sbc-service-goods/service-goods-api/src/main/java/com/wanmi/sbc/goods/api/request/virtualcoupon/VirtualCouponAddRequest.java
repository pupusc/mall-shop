package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * <p>卡券新增参数</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	@NotBlank
	@Length(max=20)
	private String name;

	/**
	 * 总数量
	 */
	@ApiModelProperty(value = "总数量")
	@Max(9999999999L)
	private Integer sumNumber;

	/**
	 * 已售总数量
	 */
	@ApiModelProperty(value = "已售总数量")
	@Max(9999999999L)
	private Integer saledNumber;

	/**
	 * 0:兑换码 1:券码+密钥 2:链接
	 */
	@ApiModelProperty(value = "0:兑换码 1:券码+密钥 2:链接")
	@NotNull
	@Max(127)
	private Integer provideType;

	/**
	 * 0:未发布 1:已发布
	 */
	@ApiModelProperty(value = "0:未发布 1:已发布")
	private Integer publishStatus;

	/**
	 * 关联的skuId
	 */
	@ApiModelProperty(value = "关联的skuId")
	@Length(max=32)
	private String skuId;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@Length(max=40)
	private String description;

	/**
	 * 删除标识;0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识;0:未删除1:已删除", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

}