package com.wanmi.sbc.setting.api.request.searchterms;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * <p>联想词VO</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Data
public class AssociationLongTailWordRequest extends SettingBaseRequest {



    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long associationLongTailWordId;


    /**
     * 联想词
     */
    @ApiModelProperty(value = "联想词")
    @Length(max = 10)
    @NotBlank
    private String associationalWord;

    /**
     * 长尾词
     */
    @ApiModelProperty(value = "长尾词")
    @Size(max=3)
    private List<String> longTailWordList;

    /**
     * 关联搜索词id
     */
    @ApiModelProperty(value = "关联搜索词id")
    @NotNull
    private Long searchAssociationalWordId;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    @NotNull
    private Long sortNumber;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String deletePerson;


}
