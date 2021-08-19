package com.wanmi.sbc.crm.api.request.autotag;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.crm.bean.dto.AutoTagSelectDTO;
import com.wanmi.sbc.crm.bean.dto.TagRuleDTO;
import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import com.wanmi.sbc.crm.bean.vo.RangeParamVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>自动标签修改参数</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoTagModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 自动标签名称
	 */
	@ApiModelProperty(value = "自动标签名称")
	@NotBlank
	@Length(max=255)
	private String tagName;

	/**
	 * 标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签
	 */
	@ApiModelProperty(value = "标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签")
	@NotNull
	private TagType type;

	/**
	 * 规则天数
	 */
	@ApiModelProperty(value = "规则天数")
	@NotNull
	private Integer day;

	/**
	 * 一级维度且或关系，0：且，1：或
	 */
	@ApiModelProperty(value = "一级维度且或关系，0：且，1：或")
//    @NotNull
	private RelationType relationType;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

	/**
	 * 一级维度-规则信息
	 */
	@ApiModelProperty(value = "一级维度-规则信息")
//    @Valid
//    @NotEmpty
	private List<TagRuleDTO> ruleList;

	@ApiModelProperty(value = "一级维度-规则信息")
	private Integer count;

	@ApiModelProperty(value = "一级维度-规则信息")
	private Integer maxLen;

	@ApiModelProperty(value = "一级维度-规则信息")
	private Map<Integer, AutoTagSelectDTO> autoTagSelectMap;

	@ApiModelProperty(value = "偏好类标签范围属性")
	private List<RangeParamVo> dataRange;

}