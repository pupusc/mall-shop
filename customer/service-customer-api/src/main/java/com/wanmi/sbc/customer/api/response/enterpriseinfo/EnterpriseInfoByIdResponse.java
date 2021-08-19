package com.wanmi.sbc.customer.api.response.enterpriseinfo;

import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）企业信息表信息response</p>
 * @author TangLian
 * @date 2020-03-03 14:11:45
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业信息表信息
     */
    @ApiModelProperty(value = "企业信息表信息")
    private EnterpriseInfoVO enterpriseInfoVO;
}
