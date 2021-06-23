package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.LevelRightsType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>会员等级权益表VO</p>
 */
@Data
public class CustomerLevelRightsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private Integer rightsId;

	/**
	 * 权益名称
	 */
	private String rightsName;

	/**
	 * 权益类型 0等级徽章 1专属客服 2会员折扣 3券礼包 4返积分
	 */
	private LevelRightsType rightsType;

	/**
	 * logo地址
	 */
	private String rightsLogo;

	/**
	 * 权益介绍
	 */
	private String rightsDescription;

	/**
	 * 权益规则(JSON)
	 */
	private String rightsRule;

	/**
	 * 活动Id
	 */
	private String activityId;

	/**
	 * 是否开启 0:关闭 1:开启
	 */
	private Integer status;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 删除标识 0:未删除1:已删除
	 */
	private DeleteFlag delFlag;

}