package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>拼团活动信息表VO</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */

@ApiModel
@Data
public class GrouponSettingVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
    @ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 拼团商品审核
	 */
    @ApiModelProperty(value = "拼团商品审核")
	private DefaultFlag goodsAuditFlag;

	/**
	 * 广告
	 */
    @ApiModelProperty(value = "广告")
	private String advert;

	/**
	 * 拼团规则
	 */
    @ApiModelProperty(value = "拼团规则")
	private String rule;

}