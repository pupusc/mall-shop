package com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个查询门店微信小程序配置请求参数</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreWechatMiniProgramConfigByStoreIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 门店id
	 */
	@ApiModelProperty(value = "门店id")
	@NotNull
	private Long storeId;
}