package com.wanmi.sbc.crm.bean.vo;

import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>自动标签List VO</p>
 * @author dyt
 * @date 2020-03-11 14:47:32
 */
@ApiModel
@Data
public class PreferenceTagListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 自动标签名称
     */
    @ApiModelProperty(value = "自动标签名称")
    private String tagName;

    /**
     * 会员人数
     */
    @ApiModelProperty(value = "会员人数")
    private Long customerCount;

    /**
     * 标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签
     */
    @ApiModelProperty(value = "标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签")
    private TagType type;

    /**
     * 规则天数
     */
    @ApiModelProperty(value = "规则天数")
    private Integer day;

    /**
     * 一级维度且或关系，0：且，1：或
     */
    @ApiModelProperty(value = "一级维度且或关系，0：且，1：或")
    private RelationType relationType;

    /**
     * 系统标识，0：非系统，1：系统
     */
    @ApiModelProperty(value = "系统标识，false：非系统，true：系统")
    private Boolean systemFlag;

}
