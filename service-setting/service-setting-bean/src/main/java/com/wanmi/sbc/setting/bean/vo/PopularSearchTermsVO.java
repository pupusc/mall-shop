package com.wanmi.sbc.setting.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>热门搜索VO</p>
 * @author weiwenhao
 * @date 2020-04-17
 */
@ApiModel
@Data
public class PopularSearchTermsVO implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 热门搜索词
     */
    @ApiModelProperty(value= "热门搜索词")
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
    @ApiModelProperty(value="排序号")
    private Long sortNumber;

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
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value= "更新人")
    private String updatePerson;


    /**
     * 删除时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value= "删除时间")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String deletePerson;

}
