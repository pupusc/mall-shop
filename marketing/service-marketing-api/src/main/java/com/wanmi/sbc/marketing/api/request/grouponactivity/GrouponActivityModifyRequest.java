package com.wanmi.sbc.marketing.api.request.grouponactivity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoForEditDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>拼团活动信息表修改参数</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@Data
public class GrouponActivityModifyRequest extends BaseRequest {

	private static final long serialVersionUID = -5880357234975551464L;

	/**
	 * 活动ID
	 */
	@ApiModelProperty(value = "活动ID")
	@NotBlank
	private String grouponActivityId;

	/**
	 * 拼团人数
	 */
	@ApiModelProperty(value = "拼团人数")
	@NotNull
	private Integer grouponNum;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTime;

	/**
	 * 拼团分类ID
	 */
	@ApiModelProperty(value = "拼团分类ID")
	@NotBlank
	private String grouponCateId;

	/**
	 * 是否自动成团
	 */
	@ApiModelProperty(value = "是否自动成团")
	@NotNull
	private boolean autoGroupon;

	/**
	 * 是否包邮，0：否，1：是
	 */
	@ApiModelProperty(value = "是否包邮")
	@NotNull
	private boolean freeDelivery;

	/**
	 * 拼团活动单品列表
	 */
	@ApiModelProperty(value = "拼团活动单品列表")
	@NotEmpty
	private List<GrouponGoodsInfoForEditDTO> goodsInfos;

	@Override
	public void checkParam() {

		// 结束时间必须在开始时间之后
		if (!endTime.isAfter(startTime)) {
			throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
		}

	}

}