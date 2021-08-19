package com.wanmi.sbc.setting.api.request.systemresourcecate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>平台素材资源分类列表查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceCateCheckChildRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;


	/**
	 * 素材资源分类id
	 */
	@ApiModelProperty(value = "素材资源分类id")
	@NotNull
	private Long cateId;


}