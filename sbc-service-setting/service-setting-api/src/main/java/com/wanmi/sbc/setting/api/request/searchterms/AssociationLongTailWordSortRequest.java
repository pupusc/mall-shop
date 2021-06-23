package com.wanmi.sbc.setting.api.request.searchterms;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>联想词VO</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class AssociationLongTailWordSortRequest extends SettingBaseRequest {



    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @NotNull
    private Long associationLongTailWordId;

    /**
     * 排序号
     */
    @ApiModelProperty(value= "排序号")
    @NotNull
    private Long sortNumber;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

}
