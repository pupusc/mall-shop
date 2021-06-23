package com.wanmi.sbc.elastic.bean.vo.searchterms;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author houshuai
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsAssociationLongTailWordVO implements Serializable {

    private static final long serialVersionUID = -1917526331875075717L;
    /**
     * 主键id
     */
    @ApiModelProperty(value= "主键id")
    private Long associationLongTailWordId;

    /**
     * 联想词
     */
    @ApiModelProperty(value = "联想词")
    private String associationalWord;

    /**
     * 长尾词
     */
    @ApiModelProperty(value= "长尾词")
    private String longTailWord;

    /**
     * 关联搜索词id
     */
    @ApiModelProperty(value= "关联搜索词id")
    private Long searchAssociationalWordId;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号")
    private Long sortNumber;

    /**
     * 是否删除 0 否  1 是
     */
    @ApiModelProperty(value = "是否删除 0 否  1 是")
    private DeleteFlag delFlag;



    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

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
    @ApiModelProperty(value = "更新人")
    private String updatePerson;


    /**
     * 删除时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    @ApiModelProperty(value = "删除人")
    private String deletePerson;

}