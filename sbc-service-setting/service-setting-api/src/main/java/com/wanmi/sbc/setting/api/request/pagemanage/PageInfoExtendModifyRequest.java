package com.wanmi.sbc.setting.api.request.pagemanage;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
public class PageInfoExtendModifyRequest extends SettingBaseRequest {

    private static final long serialVersionUID = 7000957160185339243L;

    /**
     * pageId
     */
    @ApiModelProperty(value= "页面id")
    @NotBlank
    private String pageId;

    /**
     * 页面背景图
     */
    @ApiModelProperty(value= "页面背景图")
    private String backGroundPic;

    /**
     * 使用类型  0:小程序 1:二维码
     */
    @ApiModelProperty(value= "使用类型  0:小程序 1:二维码")
    private Integer useType;

    /**
     * 来源
     */
    @ApiModelProperty(value= "来源")
    private List<String> sources;


}
