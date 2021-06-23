package com.wanmi.sbc.setting.api.request.platformaddress;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>平台地址信息新增参数</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAddressAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 地址id
	 */
	@ApiModelProperty(value = "地址id")
	@NotBlank
	@Length(max=50)
	private String addrId;

	/**
	 * 地址名称
	 */
	@ApiModelProperty(value = "地址名称")
	@NotBlank
	@Length(max=200)
	private String addrName;

	/**
	 * 父地址ID
	 */
	@ApiModelProperty(value = "父地址ID")
	@NotBlank
	@Length(max=50)
	private String addrParentId;

	/**
	 * 地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
	 */
	@ApiModelProperty(value = "地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)")
	@NotNull
	private AddrLevel addrLevel;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Integer sortNo;
}