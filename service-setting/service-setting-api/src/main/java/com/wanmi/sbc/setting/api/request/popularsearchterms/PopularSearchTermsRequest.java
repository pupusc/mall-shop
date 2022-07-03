package com.wanmi.sbc.setting.api.request.popularsearchterms;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
public class PopularSearchTermsRequest extends SettingBaseRequest {

    private static final long serialVersionUID = 7000957160185339243L;

    /**
     * 热门搜索词
     */
    @ApiModelProperty(value= "popular_search_keyword")
    @Length(max = 10)
    @NotBlank
    private String popularSearchKeyword;
    /**
     * 搜索词类型 0-H5  1-小程序
     */
    @ApiModelProperty(value= "搜索词类型 0-H5  1-小程序")
    private Integer popularChannel = 0;
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
     * 小程序热捜词
     */
    @ApiModelProperty(value = "小程序热捜词")
    private String miniHotKeyword;

    /**
     * 小程序精选词
     */
    @ApiModelProperty(value = "小程序精选词")
    private String miniSelectKeyword;

    /**
     * 排序号
     */
    @ApiModelProperty(value="sort_number")
    @NotNull
    private Long sortNumber = 1L;

    /**
     * 是否删除 0 否  1 是
     */
    @ApiModelProperty(value = "是否删除")
    private DeleteFlag delFlag;

    /**
     * 创建人
     */
    @ApiModelProperty(value= "创建人")
    private String createPerson;

    /**
     * 更新人
     */
    @ApiModelProperty(value= "更新人")
    private String updatePerson;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String deletePerson;


}
