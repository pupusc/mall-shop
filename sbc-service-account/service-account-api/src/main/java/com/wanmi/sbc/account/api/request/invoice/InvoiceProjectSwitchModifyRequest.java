package com.wanmi.sbc.account.api.request.invoice;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * 开票项目保存实体
 * Created by yuanlinling on 2017/4/25.
 */
@ApiModel
@Data
public class InvoiceProjectSwitchModifyRequest implements Serializable {

    private static final long serialVersionUID = -8636897809172064579L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String invoiceProjectSwitchId;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 是否支持开票 0 不支持 1 支持
     */
    @ApiModelProperty(value = "是否支持开票")
    @Enumerated
    private DefaultFlag isSupportInvoice;

    /**
     * 是否支持纸质发票 0 不支持 1 支持
     */
    @ApiModelProperty(value = "是否支持纸质发票")
    @Enumerated
    private DefaultFlag isPaperInvoice;

    /**
     * 是否支持增值税发票 0 不支持 1 支持
     */
    @ApiModelProperty(value = "是否支持增值税发票")
    @Enumerated
    private DefaultFlag isValueAddedTaxInvoice;
}
