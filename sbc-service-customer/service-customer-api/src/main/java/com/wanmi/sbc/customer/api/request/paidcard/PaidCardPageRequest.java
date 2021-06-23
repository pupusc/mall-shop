package com.wanmi.sbc.customer.api.request.paidcard;

import java.math.BigDecimal;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.enums.BgTypeEnum;
import com.wanmi.sbc.customer.bean.enums.EnableEnum;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>付费会员分页查询请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<String> idList;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;

	/**
	 * 背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址
	 */
	@ApiModelProperty(value = "背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址")
	private String background;

	/**
	 * 付费会员图标
	 */
	@ApiModelProperty(value = "付费会员图标")
	private String icon;

	/**
	 * 折扣率
	 */
	@ApiModelProperty(value = "折扣率")
	private BigDecimal discountRate;

	/**
	 * 规则说明
	 */
	@ApiModelProperty(value = "规则说明")
	private String rule;

	/**
	 * 付费会员用户协议
	 */
	@ApiModelProperty(value = "付费会员用户协议")
	private String agreement;

	/**
	 * 删除标识 1：删除；0：未删除
	 */
	@ApiModelProperty(value = "删除标识 1：删除；0：未删除")
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
	 * 启动禁用标识 1：启用；2：禁用
	 */
	@ApiModelProperty(value = "启动禁用标识 1：启用；2：禁用")
	private EnableEnum enable;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	private String createPerson;

	/**
	 * 修改人ID
	 */
	@ApiModelProperty(value = "修改人ID")
	private String updatePerson;

	/**
	 * 背景类型0背景色；1背景图片
	 */
	@ApiModelProperty(value = "背景类型0背景色；1背景图片")
	private BgTypeEnum bgType;

	/**
	 * 前景色
	 */
	@ApiModelProperty(value = "前景色")
	private String textColor;

	/**
	 * erp spu 编码
	 */
	@ApiModelProperty(value = "erp_spu_code")
	private String erpSpuCode;

}