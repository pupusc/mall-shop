package com.wanmi.sbc.setting.api.request.evaluateratio;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;

/**
 * <p>单个查询商品评价系数设置请求参数</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRatioByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 系数id
	 */
	@NotNull
	private String ratioId;
}