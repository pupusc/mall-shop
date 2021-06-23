package com.wanmi.sbc.customer.invoice.model.entity;


import com.wanmi.sbc.customer.bean.enums.CheckState;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 会员增专票
 * Created by CHENLI on 2017/4/21.
 */
@Data
public class CustomerInvoiceSaveRequest {
    /**
     * 增专资质ID
     */
    private Long customerInvoiceId;

    /**
     * 会员ID
     */
    private String customerId;

    /**
     * 单位全称
     */
    @NotBlank
    private String companyName;

    /**
     * 纳税人识别号
     */
    @NotBlank
    private String taxpayerNumber;

    /**
     * 单位电话
     */
    @NotBlank
    private String companyPhone;

    /**
     * 单位地址
     */
    @NotBlank
    private String companyAddress;

    /**
     * 银行基本户号
     */
    @NotBlank
    private String bankNo;

    /**
     * 开户行
     */
    @NotBlank
    private String bankName;

    /**
     * 营业执照复印件
     */
    @NotBlank
    private String businessLicenseImg;

    /**
     * 一般纳税人认证资格复印件
     */
    @NotBlank
    private String taxpayerIdentificationImg;

    /**
     * 增票资质审核状态
     */
    private CheckState checkState;
}
