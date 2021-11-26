package com.wanmi.sbc.crm.api.response.tagparam;

import com.wanmi.sbc.crm.bean.vo.TagParamVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>标签参数列表结果</p>
 * @author dyt
 * @date 2020-03-12 15:59:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagParamListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签参数列表结果
     */
    @ApiModelProperty(value = "标签参数列表结果")
    private List<TagParamVO> tagParamVOList;
}
