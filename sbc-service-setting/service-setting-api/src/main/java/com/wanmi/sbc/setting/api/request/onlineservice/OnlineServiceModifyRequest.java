package com.wanmi.sbc.setting.api.request.onlineservice;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceItemVO;
import com.wanmi.sbc.setting.bean.vo.OnlineServiceVO;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>onlineService修改参数</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * onlineService客服信息
	 */
	@ApiModelProperty(value = "onlineService客服信息")
	private OnlineServiceVO qqOnlineServerRop;

	/**
	 * onlineerviceItem座席列表
	 */
	@ApiModelProperty(value = "onlineerviceItem座席列表")
	private List<OnlineServiceItemVO> qqOnlineServerItemRopList;

	@ApiModelProperty(value = "店铺id", hidden = true)
	private Long storeId;
}