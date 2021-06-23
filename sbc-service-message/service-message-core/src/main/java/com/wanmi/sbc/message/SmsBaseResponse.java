package com.wanmi.sbc.message;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName SmsBaseResponse
 * @Description 短信平台调用接口返回公共对象
 * @Author lvzhenwei
 * @Date 2019/12/4 11:18
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsBaseResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String Code;

    private String Message;

    private String Context;

    private String BizId;

    private String RequestId;

    private String SignName;

    private String Reason;

    private String SignStatus;

    private String CreateDate;

    private String TemplateName;

    private String TemplateContent;

    private String TemplateType;

    private String TemplateCode;

    private String TemplateStatus;

}
