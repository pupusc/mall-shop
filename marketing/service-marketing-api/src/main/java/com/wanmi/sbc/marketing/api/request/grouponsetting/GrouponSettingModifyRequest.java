package com.wanmi.sbc.marketing.api.request.grouponsetting;

import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * <p>拼团活动信息表修改参数</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@Data
public class GrouponSettingModifyRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Length(max=32)
	private String id;

	/**
	 * 拼团商品审核
	 */
	private DefaultFlag goodsAuditFlag;

	/**
	 * 广告
	 */
	@Length(max=65535)
	private String advert;

	/**
	 * 拼团规则
	 */
	@Length(max=65535)
	private String rule;

}