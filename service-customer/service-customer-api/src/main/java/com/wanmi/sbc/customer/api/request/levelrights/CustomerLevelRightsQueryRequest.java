package com.wanmi.sbc.customer.api.request.levelrights;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.LevelRightsType;
import lombok.*;

import java.util.List;

/**
 * <p>会员等级权益表通用查询请求参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelRightsQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键idList
	 */
	private List<Integer> rightsIdList;

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
	 * 删除标识 0:未删除1:已删除
	 */
	private DeleteFlag delFlag;

}