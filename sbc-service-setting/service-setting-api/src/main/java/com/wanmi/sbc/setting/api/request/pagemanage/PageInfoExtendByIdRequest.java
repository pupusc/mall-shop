package com.wanmi.sbc.setting.api.request.pagemanage;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
public class PageInfoExtendByIdRequest extends SettingBaseRequest {

    private static final long serialVersionUID = 7000957160185339243L;

    @ApiModelProperty(value= "页面id")
    @NotBlank
    private String pageId;

    @ApiModelProperty(value= "页面code")
    @NotBlank
    private String pageCode;

    @ApiModelProperty(value= "页面类型")
    @NotBlank
    private String pageType;

    @ApiModelProperty(value= "页面所属平台")
    @NotBlank
    private String platform;

    @ApiModelProperty(value= "店铺id", hidden = true)
    private Long storeId;

    @ApiModelProperty(value = "首页小程序二维码", hidden = true)
    private String mainMiniQrCode;

    @ApiModelProperty(value = "首页二维码", hidden = true)
    private String mainQrCode;

}
