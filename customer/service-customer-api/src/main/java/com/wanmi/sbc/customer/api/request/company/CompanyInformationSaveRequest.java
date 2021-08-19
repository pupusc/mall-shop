package com.wanmi.sbc.customer.api.request.company;

import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: songhanlin
 * @Date: Created In 上午10:05 2017/11/1
 * @Description: 工商信息Request
 */
@ApiModel
@Data
public class CompanyInformationSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -3258053661922186792L;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @NotNull
    private Long companyInfoId;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    @NotBlank
    @Length(min = 1, max = 50)
    private String companyName;

    /**
     * 社会信用代码
     */
    @ApiModelProperty(value = "社会信用代码")
    @NotBlank
    @Length(min = 8, max = 30)
    private String socialCreditCode;

    /**
     * 住所
     */
    @ApiModelProperty(value = "住所")
    @CanEmpty
    @Length(max = 60)
    private String address;

    /**
     * 法定代表人
     */
    @ApiModelProperty(value = "法定代表人")
    @CanEmpty
    @Length(max = 10)
    private String legalRepresentative;

    /**
     * 注册资本
     */
    @ApiModelProperty(value = "注册资本")
    @CanEmpty
    private BigDecimal registeredCapital = BigDecimal.ZERO;

    /**
     * 成立日期
     */
    @ApiModelProperty(value = "成立日期")
    @CanEmpty
    private String foundDate;

    /**
     * 营业期限自
     */
    @ApiModelProperty(value = "营业期限自")
    @CanEmpty
    private String businessTermStart;

    /**
     * 营业期限至
     */
    @ApiModelProperty(value = "营业期限至")
    @CanEmpty
    private String businessTermEnd;

    /**
     * 经营范围
     */
    @ApiModelProperty(value = "经营范围")
    @NotBlank
    @Length(min = 1, max = 500)
    private String businessScope;

    /**
     * 营业执照副本电子版
     */
    @ApiModelProperty(value = "营业执照副本电子版")
    @NotBlank
    private String businessLicence;

    /**
     * 法人身份证正面
     */
    @ApiModelProperty(value = "法人身份证正面")
    @CanEmpty
    private String frontIDCard;

    /**
     * 法人身份证反面
     */
    @ApiModelProperty(value = "法人身份证反面")
    @CanEmpty
    private String backIDCard;

    @Override
    public void checkParam() {
        boolean flag = false;
        // 营业执照必填, 身份证选填
        if (StringUtils.split(businessLicence, ",").length != 1) {
            flag = true;
        } else if (StringUtils.isNotBlank(frontIDCard) && StringUtils.split(frontIDCard, ",").length > 1) {
            flag = true;
        } else if (StringUtils.isNotBlank(backIDCard) && StringUtils.split(backIDCard, ",").length > 1) {
            flag = true;
        }
        if (flag) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }
}
