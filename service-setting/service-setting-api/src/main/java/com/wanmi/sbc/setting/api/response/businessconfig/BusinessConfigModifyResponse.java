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
 * <p>招商页设置修改结果</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessConfigModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的招商页设置信息
     */
    @ApiModelProperty(value = "已修改的招商页设置信息")
    private BusinessConfigVO businessConfigVO;
}
