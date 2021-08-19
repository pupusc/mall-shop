package com.wanmi.sbc.setting.api.request.evaluateratio;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;

/**
 * <p>批量删除商品评价系数设置请求参数</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRatioDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-系数idList
	 */
	@NotEmpty
	private List<String> ratioIdList;
}