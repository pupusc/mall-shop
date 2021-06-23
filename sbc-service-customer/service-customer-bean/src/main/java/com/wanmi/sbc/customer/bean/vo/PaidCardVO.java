package com.wanmi.sbc.customer.bean.vo;

import java.math.BigDecimal;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;

import com.wanmi.sbc.customer.bean.enums.AccessType;
import com.wanmi.sbc.customer.bean.enums.BgTypeEnum;
import com.wanmi.sbc.customer.bean.enums.EnableEnum;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>付费会员VO</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@ApiModel
@Data
public class PaidCardVO implements Serializable {
	private static final long serialVersionUID = 1L;

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
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

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

	/**
	 * 付费规则
	 */
	@ApiModelProperty(value = "付费规则")
	private List<PaidCardRuleVO>ruleVOList;

	/**
	 * 权益
	 */
	@ApiModelProperty(value = "权益")
	private List<PaidCardRightsRelVO> rightsVOList;


	/**
	 * 付费会员数量
	 */
	@ApiModelProperty(value = "付费会员数量")
	private Long customerNum;


	/**
	 * 是否拥有该付费会员
	 */
	@ApiModelProperty(value = "是否拥有该付费会员")
	private Boolean isHave = false;

	/**
	 * 是否需要提示
	 */
	@ApiModelProperty(value = "是否需要提示")
	private Boolean isNeedRemain = false;

	/**
	 * 付费卡实例信息
	 */
	@ApiModelProperty(value = "是否拥有该付费会员")
	private PaidCardCustomerRelVO paidCardCustomerRel;

	private List<String> rightsNameList;

	/**
	 * 购买类型 0 用户购买 1 有赞同步
	 */
	@ApiModelProperty(name = "购买类型 0 用户购买 1 有赞同步")
	private AccessType accessType;

}