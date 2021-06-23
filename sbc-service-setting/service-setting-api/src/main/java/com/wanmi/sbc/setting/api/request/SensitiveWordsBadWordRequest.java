package com.wanmi.sbc.setting.api.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: jiaojiao
 * @Date: 2019/3/5 17:56
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SensitiveWordsBadWordRequest extends SettingBaseRequest {
    //文本内容
    private String txt;
}
