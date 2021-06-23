package com.wanmi.sbc.crm.bean.vo;

import com.wanmi.sbc.crm.bean.dto.AutoTagSelectDTO;
import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AutoTagSelectVO
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/8/26 13:43
 **/
@ApiModel
@Data
public class AutoTagSelectVO implements Serializable {

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
     *
     */
    private Integer count;

    private Integer maxLen;

    /**
     * 标签选择条件集合
     */
    @ApiModelProperty(value = "标签选择条件集合")
    private Map<Integer, AutoTagSelectDTO> autoTagSelectMap;

    @ApiModelProperty(value = "偏好类标签范围属性")
    private List<RangeParamVo> dataRange;

}
