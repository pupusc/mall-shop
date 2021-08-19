package com.wanmi.sbc.crm.api.response.tagdimension;

import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>标签维度列表结果</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签维度列表结果
     */
    @ApiModelProperty(value = "标签维度列表结果")
    private List<TagDimensionVO> tagDimensionVOList;
}
