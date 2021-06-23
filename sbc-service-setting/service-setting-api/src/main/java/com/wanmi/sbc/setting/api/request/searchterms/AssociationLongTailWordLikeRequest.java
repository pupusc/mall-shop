package com.wanmi.sbc.setting.api.request.searchterms;


import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>搜索词VO</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class AssociationLongTailWordLikeRequest extends SettingBaseRequest {


    private static final long serialVersionUID = -6827543314470103987L;


    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    @NotNull
    private String associationalWord;


}
