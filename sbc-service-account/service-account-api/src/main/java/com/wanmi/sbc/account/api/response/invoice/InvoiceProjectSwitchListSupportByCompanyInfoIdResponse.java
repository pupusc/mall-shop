package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectSwitchSupportVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 包含是否支持开票的开票项目开关列表的响应结构
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectSwitchListSupportByCompanyInfoIdResponse implements Serializable {

    private static final long serialVersionUID = -3258735756055990791L;

    /**
     * 包含是否支持开票的开票项目开关列表 {@link InvoiceProjectSwitchSupportVO}
     */
    @ApiModelProperty(value = "包含是否支持开票的开票项目开关列表")
    private List<InvoiceProjectSwitchSupportVO> invoiceProjectSwitchSupportVOList;
}
