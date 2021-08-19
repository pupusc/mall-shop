package com.wanmi.sbc.crm.api.response.tagdimension;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.bean.vo.TagDimensionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>标签维度分页结果</p>
 * @author dyt
 * @date 2020-03-12 16:00:30
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDimensionPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签维度分页结果
     */
    @ApiModelProperty(value = "标签维度分页结果")
    private MicroServicePage<TagDimensionVO> tagDimensionVOPage;
}
