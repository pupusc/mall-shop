package com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除门店微信小程序配置请求参数</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreWechatMiniProgramConfigDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-主键List
	 */
	@ApiModelProperty(value = "批量删除-主键List")
	@NotEmpty
	private List<String> idList;
}