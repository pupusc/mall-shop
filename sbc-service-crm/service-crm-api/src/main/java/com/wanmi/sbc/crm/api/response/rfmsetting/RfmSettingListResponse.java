package com.wanmi.sbc.crm.api.response.rfmsetting;

import com.wanmi.sbc.crm.bean.vo.RfmSettingVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>rfm参数配置列表结果</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmSettingListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * rfm参数配置列表结果
     */
    @ApiModelProperty(value = "rfm参数配置列表结果")
    private List<RfmSettingVO> rfmSettingVOList;
}
