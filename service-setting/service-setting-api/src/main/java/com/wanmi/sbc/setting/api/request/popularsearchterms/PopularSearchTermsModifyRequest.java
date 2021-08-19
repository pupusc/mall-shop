package com.wanmi.sbc.setting.api.request.popularsearchterms;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
public class PopularSearchTermsModifyRequest extends SettingBaseRequest {


    private static final long serialVersionUID = -550820400738826478L;
    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;

    /**
     * 热门搜索词
     */
    @ApiModelProperty(value= "popular_search_keyword")
    @Length(max = 10)
    private String popularSearchKeyword;

    /**
     * 移动端落地页
     */
    @ApiModelProperty(value= "移动端落地页")
    private String relatedLandingPage;

    /**
     * PC落地页
     */
    @ApiModelProperty(value= "PC落地页")
    private String pcLandingPage;

    /**
     * 排序号
     */
    @ApiModelProperty(value="sort_number")
    private Long sortNumber;

    /**
     * 更新人
     */
    @ApiModelProperty(value= "更新人")
    private String updatePerson;


}
