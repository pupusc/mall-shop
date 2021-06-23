package com.wanmi.sbc.setting.api.response.systemprivacypolicy;

import com.wanmi.sbc.setting.bean.vo.SystemPrivacyPolicyVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）隐私政策信息response</p>
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPrivacyPolicyByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 隐私政策信息
     */
    @ApiModelProperty(value = "隐私政策信息")
    private SystemPrivacyPolicyVO systemPrivacyPolicyVO;
}
