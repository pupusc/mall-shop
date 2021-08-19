package com.wanmi.sbc.message.api.response.smssetting;

import com.wanmi.sbc.message.bean.vo.SmsSettingVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信配置修改结果</p>
 * @author lvzhenwei
 * @date 2019-12-03 15:15:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSettingModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的短信配置信息
     */
    @ApiModelProperty(value = "已修改的短信配置信息")
    private SmsSettingVO smsSettingVO;
}
