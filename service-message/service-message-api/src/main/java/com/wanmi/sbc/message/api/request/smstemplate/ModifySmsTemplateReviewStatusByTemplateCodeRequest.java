package com.wanmi.sbc.message.api.request.smstemplate;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ModifySmsTemplateReviewStatusByTemplateCodeRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/12/26 15:24
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifySmsTemplateReviewStatusByTemplateCodeRequest extends SmsBaseRequest {

    /**
     * 模板状态，0：待审核，1：审核通过，2：审核未通过
     */
    @ApiModelProperty(value = "模板状态，0：待审核，1：审核通过，2：审核未通过")
    private ReviewStatus reviewStatus;

    /**
     * 审核原因
     */
    @ApiModelProperty(value = "审核原因")
    private String reviewReason;

    /**
     * 模板code,模板审核通过返回的模板code，发送短信时使用
     */
    @ApiModelProperty(value = "模板code,模板审核通过返回的模板code，发送短信时使用")
    private String templateCode;
}
