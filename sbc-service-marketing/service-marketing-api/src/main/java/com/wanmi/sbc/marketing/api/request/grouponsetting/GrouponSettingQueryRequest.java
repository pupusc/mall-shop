package com.wanmi.sbc.marketing.api.request.grouponsetting;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import lombok.*;
import java.util.List;

/**
 * <p>拼团活动信息表通用查询请求参数</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponSettingQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	private List<String> idList;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 拼团商品审核
	 */
	private DefaultFlag goodsAuditFlag;

	/**
	 * 广告
	 */
	private String advert;

	/**
	 * 拼团规则
	 */
	private String rule;

}