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
 * @Description 搜索栏默认搜索VO
 * @Author zh
 * @Date  2023/2/4 10:46
 */
@ApiModel
@Data
public class DefaultSearchTermsVO implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 搜索词类型 0-H5  1-小程序
     */
    @ApiModelProperty(value = "defaultChannel")
    private Integer defaultChannel ;

    /**
     * 默认搜索词
     */
    @ApiModelProperty(value= "热门搜索词")
    private String defaultSearchKeyword;

    /**
     * 移动端落地页
     */
    @ApiModelProperty(value= "落地页")
    private String relatedLandingPage;

    /**
     * 父类id
     */
    @ApiModelProperty(value= "父类id, 父类为0")
    private Long parentId;

    /**
     * 是否为父节点 (0-否，1-是)
     */
    @ApiModelProperty(value= "是否为父节点 (0-否，1-是)")
    private Boolean isParent;

    /**
     * 前图片url
     */
    @ApiModelProperty(value= "前图片url")
    private String imgBefore;

    /**
     * 后图片url
     */
    @ApiModelProperty(value= "后图片url")
    private String imgAfter;

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
}
