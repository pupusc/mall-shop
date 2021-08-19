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
public class PopularSearchTermsDeleteRequest extends SettingBaseRequest {


    private static final long serialVersionUID = -90736848682463011L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

}
