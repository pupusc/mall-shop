package com.wanmi.sbc.message.api.request.smstemplate;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncPlatformHistorySmsTemplateRequest extends SmsBaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 批量同步短信平台短信模板--短信模板code list
     */
    @ApiModelProperty(value = "批量同步短信平台短信模板--短信模板code list")
    private List<String> templateCodeList;
}