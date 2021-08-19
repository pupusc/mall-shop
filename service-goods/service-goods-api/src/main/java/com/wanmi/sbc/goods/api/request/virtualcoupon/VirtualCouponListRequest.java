package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>卡券列表查询请求参数</p>
 * @author 梁善
 * @date 2021-01-25 10:19:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-电子卡券IDList
	 */
	@ApiModelProperty(value = "批量查询-电子卡券IDList")
	private List<Long> idList;

	/**
	 * 电子卡券ID
	 */
	@ApiModelProperty(value = "电子卡券ID")
	private Long id;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;

	/**
	 * 总数量
	 */
	@ApiModelProperty(value = "总数量")
	private Integer sumNumber;

	/**
	 * 已售总数量
	 */
	@ApiModelProperty(value = "已售总数量")
	private Integer saledNumber;

	/**
	 * 0:兑换码 1:券码+密钥 2:链接
	 */
	@ApiModelProperty(value = "0:兑换码 1:券码+密钥 2:链接")
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
	private String skuId;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String description;

	/**
	 * 删除标识;0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识;0:未删除1:已删除")
	private DeleteFlag delFlag;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

}