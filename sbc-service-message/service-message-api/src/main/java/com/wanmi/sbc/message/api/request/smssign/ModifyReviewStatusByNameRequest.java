package com.wanmi.sbc.message.api.request.smssign;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import com.wanmi.sbc.message.bean.enums.ReviewStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ModifyReviewStatusBynameRequest
 * @Description 根据签名名称更新签名状态
 * @Author lvzhenwei
 * @Date 2019/12/26 14:56
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyReviewStatusByNameRequest extends SmsBaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 签名名称
     */
    @ApiModelProperty(value = "签名名称")
    private String smsSignName;

    /**
     * 审核状态：0:待审核，1:审核通过，2:审核未通过
     */
    @ApiModelProperty(value = "审核状态：0:待审核，1:审核通过，2:审核未通过")
    private ReviewStatus reviewStatus;

    /**
     * 审核原因
     */
    @ApiModelProperty(value = "审核原因")
    private String reviewReason;
}
