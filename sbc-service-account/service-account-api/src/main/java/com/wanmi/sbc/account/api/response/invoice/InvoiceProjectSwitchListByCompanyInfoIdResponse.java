package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 开票项目开关列表响应结果
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectSwitchListByCompanyInfoIdResponse  implements Serializable {

    private static final long serialVersionUID = -8174025486219049886L;

    /**
     * 开票项目开关列表 {@link InvoiceProjectSwitchVO}
     */
    @ApiModelProperty(value = "开票项目开关列表")
    private List<InvoiceProjectSwitchVO> invoiceProjectSwitchVOList;
}
