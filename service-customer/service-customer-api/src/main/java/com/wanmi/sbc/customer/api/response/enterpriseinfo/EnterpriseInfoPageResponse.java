package com.wanmi.sbc.customer.api.response.enterpriseinfo;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>企业信息表分页结果</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业信息表分页结果
     */
    @ApiModelProperty(value = "企业信息表分页结果")
    private MicroServicePage<EnterpriseInfoVO> enterpriseInfoVOPage;
}
