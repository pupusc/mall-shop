package com.wanmi.sbc.message.api.request.smsbase;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import com.wanmi.sbc.message.bean.dto.SmsTemplateParamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>短信发送请求参数</p>
 * @author dyt
 * @date 2019-12-03 15:36:05
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

    /**
     * 接收短信的号码
     */
    @ApiModelProperty(value = "接收短信的号码")
    private String phoneNumbers;

    /**
     * 模板可变参数
     */
    @ApiModelProperty(value = "模板可变参数")
    private SmsTemplateParamDTO templateParamDTO;

    /**
     * 业务类型  参照com.wanmi.sbc.customer.bean.enums.SmsTemplate
     */
    @ApiModelProperty(value = "业务类型")
    private String businessType;
}