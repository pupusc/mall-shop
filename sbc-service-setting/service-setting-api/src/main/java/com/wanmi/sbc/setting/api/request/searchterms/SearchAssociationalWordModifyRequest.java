package com.wanmi.sbc.setting.api.request.searchterms;


import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>搜索词VO</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class SearchAssociationalWordModifyRequest extends SettingBaseRequest {


    private static final long serialVersionUID = -6827543314470103987L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @NotNull
    private Long id;

    /**
     * 搜索词
     */
    @ApiModelProperty(value = "搜索词")
    @Length(max = 10)
    @NotBlank
    private String searchTerms;


    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;


}
