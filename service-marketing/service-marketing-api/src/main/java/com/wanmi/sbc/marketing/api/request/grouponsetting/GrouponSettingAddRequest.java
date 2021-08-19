package com.wanmi.sbc.marketing.api.request.grouponsetting;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.api.request.market.MarketingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;


/**
 * <p>拼团活动信息表新增参数</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@ApiModel
@Data
public class GrouponSettingAddRequest  {
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团商品审核
	 */
    @ApiModelProperty(value = "拼团商品审核")
	private DefaultFlag goodsAuditFlag;

	/**
	 * 广告
	 */
    @ApiModelProperty(value = "广告")
	@Length(max=65535)
	private String advert;

	/**
	 * 拼团规则
	 */
    @ApiModelProperty(value = "拼团规则")
	@Length(max=65535)
	private String rule;

}