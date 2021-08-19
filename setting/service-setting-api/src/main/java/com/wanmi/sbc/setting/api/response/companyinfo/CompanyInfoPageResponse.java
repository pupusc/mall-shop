package com.wanmi.sbc.setting.api.response.companyinfo;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.CompanyInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>公司信息分页结果</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 公司信息分页结果
     */
    @ApiModelProperty(value = "公司信息分页结果")
    private MicroServicePage<CompanyInfoVO> companyInfoVOPage;
}
