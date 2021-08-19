package com.wanmi.sbc.account.api.response.invoice;

import com.wanmi.sbc.account.bean.vo.InvoiceProjectVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 开票项目分页响应
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProjectPageByCompanyInfoIdResponse implements Serializable {

    private static final long serialVersionUID = -8750303613451260830L;

    /**
     * 开票项目分页数据 {@link InvoiceProjectVO}
     */
    @ApiModelProperty(value = "开票项目分页数据")
    private MicroServicePage<InvoiceProjectVO> invoiceProjectVOPage;

}
