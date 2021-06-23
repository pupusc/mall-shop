package com.wanmi.sbc.crm.api.response.tagdimension;

import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）标签维度信息response</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签维度信息
     */
    @ApiModelProperty(value = "标签维度信息")
    private TagDimensionVO tagDimensionVO;
}
