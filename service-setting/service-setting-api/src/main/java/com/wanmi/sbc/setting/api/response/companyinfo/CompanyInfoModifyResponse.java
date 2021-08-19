package com.wanmi.sbc.setting.api.response.companyinfo;

import com.wanmi.sbc.setting.bean.vo.CompanyInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>公司信息修改结果</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的公司信息信息
     */
    @ApiModelProperty(value = "已修改的公司信息信息")
    private CompanyInfoVO companyInfoVO;
}
