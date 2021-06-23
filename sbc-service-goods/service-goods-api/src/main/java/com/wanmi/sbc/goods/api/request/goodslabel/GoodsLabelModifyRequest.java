package com.wanmi.sbc.goods.api.request.goodslabel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>商品标签修改参数</p>
 * @author dyt
 * @date 2020-09-29 13:57:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsLabelModifyRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 标签id
	 */
	@NotNull
	@ApiModelProperty(value = "标签id")
	private Long goodsLabelId;

	/**
	 * 标签名称
	 */
	@ApiModelProperty(value = "标签名称")
	@NotBlank
	@Length(max=4)
	private String labelName;

	/**
	 * 前端是否展示 false: 关闭 true:开启
	 */
	@ApiModelProperty(value = "前端是否展示 false: 关闭 true:开启")
    @NotNull
	private Boolean labelVisible;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	@Max(99L)
	private Integer labelSort;

	/**
	 * 店铺id
	 */
	@ApiModelProperty(value = "店铺id", hidden = true)
	private Long storeId;

	/**
	 * 删除标识 0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识 0:未删除1:已删除", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 重写敏感词，用于验证
	 * @return 拼凑关键内容
	 */
	@Override
	public String checkSensitiveWord(){
		return labelName;
	}

}