package com.wanmi.sbc.customer.api.response.invoice;

import com.wanmi.sbc.customer.bean.vo.CustomerInvoiceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel
@Data
@Builder(toBuilder=true)
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomerInvoiceByCustomerIdResponse implements Serializable {
    /**
     * 是否有增票资质
     */
    @ApiModelProperty(value = "是否有增票资质")
    private boolean flag = Boolean.FALSE;

    /**
     * 是否支持增票资质
     */
    @ApiModelProperty(value = "是否支持增票资质")
    private boolean configFlag = Boolean.FALSE;

    /**
     * 是否支持纸质发票
     */
    @ApiModelProperty(value = "是否支持纸质发票")
    private boolean paperInvoice = Boolean.FALSE;

    /**
     * 是否支持开票 pc端使用
     */
    @ApiModelProperty(value = "是否支持开票")
    private boolean support = Boolean.FALSE;

    /**
     * 商家Id
     */
    @ApiModelProperty(value = "商家Id")
    private Long companyInfoId;

    /**
     * 增票资质信息
     */
    @ApiModelProperty(value = "增票资质信息")
    private CustomerInvoiceVO customerInvoiceResponse;
}
