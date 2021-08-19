package com.wanmi.sbc.customer.api.response.company;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 公司信息分页
 * @Author: daiyitian
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 公司信息Response
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoPageResponse implements Serializable {

    private static final long serialVersionUID = 6492765528117007884L;
    /**
     * 公司信息列表
     */
    @ApiModelProperty(value = "公司信息列表")
    private MicroServicePage<CompanyInfoVO> companyInfoVOPage;
}
