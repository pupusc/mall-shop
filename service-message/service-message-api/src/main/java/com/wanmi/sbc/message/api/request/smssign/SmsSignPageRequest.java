package com.wanmi.sbc.message.api.request.smssign;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.message.bean.enums.InvolveThirdInterest;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import com.wanmi.sbc.message.bean.enums.SignSource;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信签名分页查询请求参数</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:49:24
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<Long> idList;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 签名名称
	 */
	@ApiModelProperty(value = "签名名称")
	private String smsSignName;

	/**
	 * 签名来源,0：企事业单位的全称或简称,1：工信部备案网站的全称或简称,2：APP应用的全称或简称,3：公众号或小程序的全称或简称,4：电商平台店铺名的全称或简称,5：商标名的全称或简称
	 */
	@ApiModelProperty(value = "签名来源,0：企事业单位的全称或简称,1：工信部备案网站的全称或简称,2：APP应用的全称或简称,3：公众号或小程序的全称或简称,4：电商平台店铺名的全称或简称,5：商标名的全称或简称")
	private SignSource signSource;

	/**
	 * 短信签名申请说明
	 */
	@ApiModelProperty(value = "短信签名申请说明")
	private String remark;

	/**
	 * 是否涉及第三方利益：0：否，1：是
	 */
	@ApiModelProperty(value = "是否涉及第三方利益：0：否，1：是")
	private InvolveThirdInterest involveThirdInterest;

	/**
	 * 审核状态：0:待审核，1:审核通过，2:审核未通过
	 */
	@ApiModelProperty(value = "审核状态：0:待审核，1:审核通过，2:审核未通过")
	private ReviewStatus reviewStatus;

	/**
	 * 审核原因
	 */
	@ApiModelProperty(value = "审核原因")
	private String reviewReason;

	/**
	 * 短信配置id
	 */
	@ApiModelProperty(value = "短信配置id")
	private Long smsSettingId;

	/**
	 * 删除标识，0：未删除，1：已删除
	 */
	@ApiModelProperty(value = "删除标识，0：未删除，1：已删除")
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

}