package com.wanmi.sbc.crm.api.response.tagdimension;

import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionBigJsonResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 偏好类标签列表
     */
    @ApiModelProperty(value = "偏好类标签指标行为列表")
    private List<TagDimensionVO> preferenceList;

    /**
     * 偏好类标签列表
     */
    @ApiModelProperty(value = "偏好类标签指标行为条件列表")
    private List<TagDimensionVO> preferenceParamList;

    /**
     * 范围类和综合类标签列表
     */
    @ApiModelProperty(value = "范围类和综合类标签列表")
    private List<TagDimensionVO> otherList;

    /**
     * 指标值类标签列表
     */
    @ApiModelProperty(value = "指标值类标签列表")
    private List<TagDimensionVO> quotaList;




}
