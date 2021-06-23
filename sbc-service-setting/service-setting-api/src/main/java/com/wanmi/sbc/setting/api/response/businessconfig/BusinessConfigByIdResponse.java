package com.wanmi.sbc.setting.api.response.businessconfig;

import com.wanmi.sbc.setting.bean.vo.BusinessConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）招商页设置信息response</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessConfigByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 招商页设置信息
     */
    @ApiModelProperty(value = "招商页设置信息")
    private BusinessConfigVO businessConfigVO;
}
