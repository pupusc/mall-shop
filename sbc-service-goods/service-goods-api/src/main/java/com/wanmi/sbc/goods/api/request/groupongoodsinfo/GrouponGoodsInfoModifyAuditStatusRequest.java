package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据活动ID批量更新审核状态</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoModifyAuditStatusRequest implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团活动ID集合
	 */
	@ApiModelProperty(value = "拼团活动ID集合")
	@NonNull
	private List<String> grouponActivityIds;

	/**
	 * 审核状态
	 */
	@ApiModelProperty(value = " 审核状态")
	@NonNull
	private AuditStatus auditStatus;
}