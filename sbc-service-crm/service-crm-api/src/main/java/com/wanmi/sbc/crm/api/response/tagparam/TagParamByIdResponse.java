package com.wanmi.sbc.crm.api.response.tagparam;

import com.wanmi.sbc.crm.bean.vo.TagParamVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）标签参数信息response</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagParamByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签参数信息
     */
    @ApiModelProperty(value = "标签参数信息")
    private TagParamVO tagParamVO;
}
