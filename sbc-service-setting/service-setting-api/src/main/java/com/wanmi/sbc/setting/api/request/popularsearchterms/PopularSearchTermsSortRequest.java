package com.wanmi.sbc.setting.api.request.popularsearchterms;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
public class PopularSearchTermsSortRequest extends SettingBaseRequest {


    private static final long serialVersionUID = 3934358860369303931L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @NotNull
    private Long id;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    @NotNull
    private Long sortNumber;


}
